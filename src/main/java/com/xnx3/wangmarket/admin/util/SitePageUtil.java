package com.xnx3.wangmarket.admin.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 栏目代码与生成页面路径的统一校验。 */
public final class SitePageUtil {
    private SitePageUtil() { }

    public static boolean isValidColumnCodeOrEmpty(String code) {
        return code == null || code.matches("[A-Za-z0-9_-]*");
    }

    public static String columnCodeError(String fieldName, String code) {
        if (isValidColumnCodeOrEmpty(code)) {
            return null;
        }
        Matcher invalid = Pattern.compile("[^A-Za-z0-9_-]").matcher(code);
        invalid.find();
        return fieldName + "的第 " + (invalid.start() + 1)
                + " 个字符不符合规则；仅允许英文字母、数字、下划线(_)和短横线(-)";
    }

    public static boolean isSafeRelativePath(String path) {
        return relativePathError(path) == null;
    }

    public static String relativePathError(String path) {
        return relativePathError(path, "页面保存路径");
    }

    public static String relativePathError(String path, String fieldName) {
        if (path == null || path.length() == 0) {
            return fieldName + "为空，请指定文件名";
        }
        Matcher invalid = Pattern.compile("[^\\p{L}\\p{N}_./-]").matcher(path);
        if (invalid.find()) {
            return fieldName + "的第 " + (invalid.start() + 1)
                    + " 个字符不符合规则；仅允许文字、数字、下划线、短横线、点号和分隔目录的正斜杠(/)";
        }
        String[] parts = path.split("/", -1);
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.length() == 0) {
                return fieldName + "的第 " + (i + 1) + " 段为空；不能以 / 开头或结尾，也不能出现连续的 /";
            }
            if (part.equals(".") || part.equals("..")) {
                return fieldName + "的第 " + (i + 1) + " 段是“" + part + "”；不允许使用当前目录或上级目录标记";
            }
            if (part.endsWith(".")) {
                return fieldName + "的第 " + (i + 1) + " 段以点号结尾；请移除结尾的点号";
            }
        }
        return null;
    }

    public static void checkLocalPath(String localRoot, String storagePath) throws IOException {
        if (localRoot == null || localRoot.length() == 0) {
            throw new IOException("本地文件存储根目录未配置");
        }
        String pathError = relativePathError(storagePath);
        if (pathError != null) {
            throw new IOException(pathError);
        }
        Path root = new File(localRoot).toPath().toAbsolutePath().normalize();
        // 存储组件直接拼接根目录和文件路径，检查时保持相同规则。
        Path target = new File(localRoot + storagePath).toPath().toAbsolutePath().normalize();
        if (!target.equals(root.resolve(storagePath))) {
            throw new IOException("本地文件存储根目录末尾缺少目录分隔符，拼接后的文件路径不在配置的目录内");
        }
        // 从已有父目录解析，使路径校验不依赖目标文件是否已创建。
        Path existing = root;
        while (!Files.exists(existing, LinkOption.NOFOLLOW_LINKS)) {
            existing = existing.getParent();
        }
        Path current = existing.toRealPath().resolve(existing.relativize(root));
        for (String part : storagePath.split("/")) {
            current = current.resolve(part);
            if (Files.isSymbolicLink(current)
                    || (Files.exists(current, LinkOption.NOFOLLOW_LINKS)
                        && !current.toRealPath().equals(current))) {
                throw new IOException("本地文件路径“" + storagePath
                        + "”包含符号链接或指向其他位置的目录；请使用站点目录内的普通文件和目录");
            }
        }
    }
}
