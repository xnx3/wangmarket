package com.xnx3.wangmarket.admin.util;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import javax.servlet.http.HttpServletRequest;

/** 模板编码及本地资源目录的统一校验。 */
public final class TemplateCodeUtil {
    private TemplateCodeUtil() { }

    public static boolean isValid(String code) {
        return code != null && code.matches("[A-Za-z0-9_-]{1,20}");
    }

    public static boolean isValidOrEmpty(String code) {
        return code == null || code.length() == 0 || isValid(code);
    }

    public static File directory(String projectPath, String code) throws IOException {
        if (!isValid(code)) {
            throw new IOException("模板编码格式不正确");
        }
        // File 兼容运行环境返回的 /C:/... 形式的 Windows 路径。
        Path project = new File(projectPath).toPath().toRealPath();
        Path base = project.resolve("websiteTemplate");
        Path directory = base.resolve(code);
        checkEntry(base);
        checkEntry(directory);
        if (Files.exists(directory, LinkOption.NOFOLLOW_LINKS) && !Files.isDirectory(directory)) {
            throw new IOException("模板资源目录不正确");
        }
        return directory.toFile();
    }

    public static File archive(String projectPath, String code) throws IOException {
        if (!isValid(code)) {
            throw new IOException("模板编码格式不正确");
        }
        Path project = new File(projectPath).toPath().toRealPath();
        Path data = project.resolve("plugin_data");
        Path exports = data.resolve("templateDevelop");
        Path archive = exports.resolve("template_" + code + ".zip");
        checkEntry(data);
        checkEntry(exports);
        checkEntry(archive);
        if (Files.exists(archive, LinkOption.NOFOLLOW_LINKS) && !Files.isRegularFile(archive)) {
            throw new IOException("模板导出文件不正确");
        }
        return archive.toFile();
    }

    private static void checkEntry(Path path) throws IOException {
        if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)
                && (Files.isSymbolicLink(path) || !path.toRealPath().equals(path.toAbsolutePath().normalize()))) {
            throw new IOException("模板资源路径不正确");
        }
    }

    public static void checkFiles(File directory) throws IOException {
        checkEntry(directory.toPath());
        if (!Files.exists(directory.toPath(), LinkOption.NOFOLLOW_LINKS)) {
            return;
        }
        Files.walkFileTree(directory.toPath(), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attributes) throws IOException {
                checkEntry(path);
                if (attributes.isSymbolicLink()) {
                    throw new IOException("模板资源目录不正确");
                }
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attributes) throws IOException {
                checkEntry(path);
                if (!attributes.isRegularFile()) {
                    throw new IOException("模板资源文件不正确");
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static boolean isLocalDevelopment(HttpServletRequest request, String configuredUrl) {
        if (!isLoopback(request.getRemoteAddr()) || !isLoopback(request.getServerName())
                || request.getHeader("Forwarded") != null || request.getHeader("X-Forwarded-For") != null) {
            return false;
        }
        try {
            // 同时使用服务器配置，避免将反向代理的本地连接视作开发访问。
            return configuredUrl != null && isLoopback(new URI(configuredUrl).getHost());
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isLoopback(String host) {
        if ("localhost".equalsIgnoreCase(host)) {
            return true;
        }
        if (host == null || !(host.matches("127(?:\\.[0-9]{1,3}){3}") || host.indexOf(':') >= 0)) {
            return false;
        }
        try {
            return InetAddress.getByName(host).isLoopbackAddress();
        } catch (IOException e) {
            return false;
        }
    }
}
