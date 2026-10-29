package com.xnx3.wangmarket.admin.util;

import java.io.File;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import com.baidu.ueditor.ActionEnter;
import com.baidu.ueditor.ConfigManager;
import com.baidu.ueditor.define.ActionMap;
import com.xnx3.j2ee.util.SystemUtil;

/** UEditor 操作入口，远程图片使用与普通上传相同的会话权限。 */
public class UEditorActionEnter extends ActionEnter {
    private final HttpServletRequest request;
    private final String rootPath;

    public UEditorActionEnter(HttpServletRequest request, String rootPath) {
        super(request, rootPath);
        this.request = request;
        this.rootPath = rootPath;
    }

    @Override
    public String invoke() {
        String action = request.getParameter("action");
        if (!ActionMap.mapping.containsKey(action) || ActionMap.getType(action) != ActionMap.CATCH_IMAGE) {
            return super.invoke();
        }
        if (!SessionUtil.isLogin()) {
            return result("请先登陆");
        }
        int requested = 0;
        int succeeded = 0;
        String outcome = "失败";
        try {
            if (!SessionUtil.isAllowUploadForUEditor()) {
                return result("不允许上传文件，可能是您允许上传的存储空间已满");
            }
            String configRoot = rootPath;
            String normalized = rootPath.replace(File.separatorChar, '/');
            if (normalized.endsWith("/src/main/webapp/")) {
                String projectPath = SystemUtil.getProjectPath();
                if (projectPath.endsWith("/target/classes/")) {
                    configRoot = projectPath;
                }
            }
            ConfigManager manager = ConfigManager.getInstance(configRoot, request.getContextPath(), request.getRequestURI());
            if (manager == null || !manager.valid()) {
                return result("配置加载失败");
            }
            Map<String, Object> config = manager.getConfig(ActionMap.CATCH_IMAGE);
            String[] sources = request.getParameterValues((String) config.get("fieldName"));
            requested = sources == null ? 0 : sources.length;
            if (requested > RemoteImageCapture.MAX_IMAGES) {
                return result("本次图片数量过多，请分批处理");
            }
            JSONArray list = new JSONArray();
            try (RemoteImageCapture capture = new RemoteImageCapture(config)) {
                if (sources != null) {
                    for (String source : sources) {
                        JSONObject item = capture.capture(source);
                        list.put(item);
                        if ("SUCCESS".equals(item.optString("state"))) {
                            succeeded++;
                        }
                    }
                }
            }
            outcome = "完成";
            return new JSONObject().put("state", "SUCCESS").put("list", list).toString();
        } catch (Exception e) {
            return result("图片抓取失败");
        } finally {
            ActionLogUtil.insert(request, "抓取远程图片", "请求：" + requested + "，成功：" + succeeded + "，结果：" + outcome);
        }
    }

    private static String result(String message) {
        return new JSONObject().put("state", message).put("list", new JSONArray()).toString();
    }
}
