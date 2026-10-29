package com.xnx3.wangmarket.admin.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.DnsResolver;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import com.baidu.ueditor.ConfigManager;
import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.MIMEType;
import com.xnx3.j2ee.util.SystemUtil;
import cn.zvo.fileupload.vo.UploadFileVO;

/** 远程图片连接及保存，使用独立客户端，不继承代理、Cookie 或登录凭据。 */
final class RemoteImageCapture implements Closeable {
    static final int MAX_IMAGES = 100;
    private static final int MAX_REDIRECTS = 5;
    private final Map<String, Object> config;
    private final CloseableHttpClient client;
    private final Set<InetAddress> serverAddresses = new HashSet<InetAddress>();
    private final long batchDeadline = System.nanoTime() + TimeUnit.MINUTES.toNanos(2);

    RemoteImageCapture(Map<String, Object> config) throws IOException {
        this.config = config;
        String serverUrl = SystemUtil.get("MASTER_SITE_URL");
        if (serverUrl != null && !serverUrl.trim().isEmpty()) {
            try {
                String serverHost = URI.create(serverUrl.trim()).getHost();
                if (serverHost == null) {
                    throw new IOException("服务器地址配置不正确");
                }
                serverAddresses.addAll(Arrays.asList(InetAddress.getAllByName(serverHost)));
            } catch (IllegalArgumentException e) {
                throw new IOException("服务器地址配置不正确", e);
            }
        }
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000)
                .setSocketTimeout(5000).setConnectionRequestTimeout(5000).build();
        this.client = HttpClients.custom().setDefaultRequestConfig(requestConfig)
                .setDnsResolver(new DnsResolver() {
                    @Override
                    public InetAddress[] resolve(String host) throws UnknownHostException {
                        return resolveAllowedAddresses(host);
                    }
                }).disableRedirectHandling().disableAutomaticRetries().disableCookieManagement()
                .disableAuthCaching().disableContentCompression().build();
    }

    JSONObject capture(String source) {
        JSONObject result = new JSONObject().put("source", source == null ? "" : source);
        File temporary = null;
        long deadline = Math.min(batchDeadline, System.nanoTime() + TimeUnit.SECONDS.toNanos(30));
        try {
            URI target = new URI(source);
            for (int hop = 0; hop <= MAX_REDIRECTS; hop++) {
                checkDeadline(deadline);
                validateTarget(target);
                HttpGet request = new HttpGet(target);
                request.setHeader("Accept", "image/*");
                try (CloseableHttpResponse response = client.execute(request)) {
                    int status = response.getStatusLine().getStatusCode();
                    if (status == 301 || status == 302 || status == 303 || status == 307 || status == 308) {
                        Header location = response.getFirstHeader("Location");
                        if (location == null || hop == MAX_REDIRECTS) {
                            throw new IOException("图片跳转失败");
                        }
                        target = target.resolve(new URI(location.getValue()));
                        continue;
                    }
                    HttpEntity entity = response.getEntity();
                    if (status != 200 || entity == null) {
                        throw new IOException("图片读取失败");
                    }
                    String suffix = imageSuffix(entity.getContentType());
                    long maxSize = ((Number) config.get("maxSize")).longValue();
                    if (maxSize <= 0 || entity.getContentLength() > maxSize) {
                        throw new IOException("图片大小超出限制");
                    }
                    temporary = File.createTempFile("ueditor-image-", ".tmp");
                    try (InputStream input = entity.getContent(); OutputStream output = Files.newOutputStream(temporary.toPath())) {
                        copy(input, output, maxSize, deadline);
                    }
                    String path = PathFormat.parse((String) config.get("savePath") + suffix, (String) config.get("filename"));
                    // 路径来自编辑器配置及会话，保存前仍校验每一段。
                    path = path.replaceFirst("^/", "");
                    if (!path.matches("[a-zA-Z0-9_-]+(?:/[a-zA-Z0-9_-]+)*\\.(png|jpg|jpeg|gif|bmp)")) {
                        throw new IOException("图片保存路径不正确");
                    }
                    UploadFileVO uploaded;
                    try (InputStream input = new FileInputStream(temporary)) {
                        uploaded = ConfigManager.getFileUpload().upload(path, input, false);
                    }
                    if (uploaded == null || uploaded.getResult() != UploadFileVO.SUCCESS) {
                        throw new IOException("图片保存失败");
                    }
                    return result.put("state", "SUCCESS").put("url", uploaded.getUrl());
                }
            }
        } catch (Exception e) {
            return result.put("state", "图片抓取失败或地址不允许访问");
        } finally {
            if (temporary != null) {
                try {
                    Files.deleteIfExists(temporary.toPath());
                } catch (IOException e) {
                    temporary.deleteOnExit();
                }
            }
        }
        return result.put("state", "图片抓取失败");
    }

    private String imageSuffix(Header contentType) throws IOException {
        String mime = contentType == null ? "" : contentType.getValue().split(";", 2)[0].trim().toLowerCase(Locale.ROOT);
        String suffix = MIMEType.getSuffix(mime);
        List<String> allowed = Arrays.asList((String[]) config.get("allowFiles"));
        if (suffix == null || !allowed.contains(suffix)) {
            throw new IOException("不支持的图片类型");
        }
        return suffix;
    }

    private void validateTarget(URI target) throws IOException {
        String scheme = target.getScheme();
        String host = target.getHost();
        if (!("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme))
                || host == null || target.getRawUserInfo() != null
                || target.getPort() == 0 || target.getPort() > 65535) {
            throw new IOException("图片地址不正确");
        }
        String normalized = host.toLowerCase(Locale.ROOT).replaceFirst("\\.$", "");
        for (String excluded : (String[]) config.get("filter")) {
            if (normalized.equalsIgnoreCase(excluded.replaceFirst("\\.$", ""))) {
                throw new IOException("图片地址不允许访问");
            }
        }
        resolveAllowedAddresses(host);
    }

    private InetAddress[] resolveAllowedAddresses(String host) throws UnknownHostException {
        InetAddress[] addresses = resolvePublicAddresses(host);
        // 同时排除后台配置的服务入口，兼顾公网地址映射的部署方式。
        for (InetAddress address : addresses) {
            if (serverAddresses.contains(address)) {
                throw new UnknownHostException("图片地址不允许访问");
            }
        }
        return addresses;
    }

    static InetAddress[] resolvePublicAddresses(String host) throws UnknownHostException {
        InetAddress[] addresses = InetAddress.getAllByName(host);
        if (addresses.length == 0) {
            throw new UnknownHostException("图片地址不可用");
        }
        // 连接层只接收本次已验证的地址，不再进行未受控的域名解析。
        for (InetAddress address : addresses) {
            if (!isPublicAddress(address)) {
                throw new UnknownHostException("图片地址不允许访问");
            }
        }
        return addresses;
    }

    static boolean isPublicAddress(InetAddress address) {
        if (address.isAnyLocalAddress() || address.isLoopbackAddress() || address.isLinkLocalAddress()
                || address.isSiteLocalAddress() || address.isMulticastAddress()) {
            return false;
        }
        try {
            if (NetworkInterface.getByInetAddress(address) != null) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        byte[] bytes = address.getAddress();
        int first = bytes[0] & 255;
        int second = bytes[1] & 255;
        if (bytes.length == 4) {
            int third = bytes[2] & 255;
            return !(first == 0 || first == 10 || first == 127 || first >= 224
                    || (first == 100 && second >= 64 && second <= 127)
                    || (first == 169 && second == 254)
                    || (first == 172 && second >= 16 && second <= 31)
                    || (first == 192 && (second == 168 || (second == 0 && (third == 0 || third == 2))
                        || (second == 88 && third == 99)))
                    || (first == 198 && (second == 18 || second == 19 || (second == 51 && third == 100)))
                    || (first == 203 && second == 0 && third == 113));
        }
        // IPv6 只接受常规公网单播，排除特殊用途及转换地址段。
        return bytes.length == 16 && (first & 224) == 32
                && !(first == 32 && second == 1 && ((bytes[2] & 254) == 0
                    || ((bytes[2] & 255) == 13 && (bytes[3] & 255) == 184)))
                && !(first == 32 && second == 2)
                && !(first == 63 && second == 255 && (bytes[2] & 240) == 0);
    }

    static void copy(InputStream input, OutputStream output, long maxSize, long deadline) throws IOException {
        byte[] buffer = new byte[8192];
        long total = 0;
        int count;
        while ((count = input.read(buffer)) != -1) {
            checkDeadline(deadline);
            total += count;
            if (total > maxSize) {
                throw new IOException("图片大小超出限制");
            }
            output.write(buffer, 0, count);
        }
    }

    private static void checkDeadline(long deadline) throws IOException {
        if (System.nanoTime() >= deadline) {
            throw new IOException("图片读取超时");
        }
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}
