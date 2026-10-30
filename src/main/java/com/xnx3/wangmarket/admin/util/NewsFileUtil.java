package com.xnx3.wangmarket.admin.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import com.xnx3.j2ee.util.AttachmentUtil;
import com.xnx3.j2ee.vo.BaseVO;
import com.xnx3.wangmarket.admin.entity.News;
import cn.zvo.fileupload.StorageInterface;
import cn.zvo.fileupload.storage.local.LocalStorage;

/** 文章头图及静态页面的统一清理。 */
public final class NewsFileUtil {
    private NewsFileUtil() { }

    public static BaseVO deleteTitlePicture(News news) {
        String picture = news.getTitlepic();
        if (picture == null || picture.length() == 0
                || picture.regionMatches(true, 0, "http://", 0, 7)
                || picture.regionMatches(true, 0, "https://", 0, 8)
                || picture.startsWith("//")) {
            return BaseVO.success();
        }
        String pathError = SitePageUtil.relativePathError(picture, "头图文件路径");
        if (pathError != null) {
            return BaseVO.failure(pathError);
        }
        return deleteFile(news.getSiteid(), "news/" + picture);
    }

    public static BaseVO deleteHtmlPage(Integer siteId, String pageName) {
        String pathError = SitePageUtil.relativePathError(pageName, "文章 HTML 页面名称");
        if (pathError != null) {
            return BaseVO.failure(pathError);
        }
        if (pageName.indexOf('/') >= 0) {
            return BaseVO.failure("文章 HTML 页面名称不能包含目录，请填写不含目录和 .html 后缀的文件名");
        }
        return deleteFile(siteId, pageName + ".html");
    }

    private static BaseVO deleteFile(Integer siteId, String relativePath) {
        if (siteId == null || siteId <= 0) {
            return BaseVO.failure("文章所属站点编号无效，无法确定文件清理范围");
        }
        String storagePath = "site/" + siteId + "/" + relativePath;
        try {
            StorageInterface storage = AttachmentUtil.getStorageMode();
            if (storage == null) {
                return BaseVO.failure("文件存储组件未配置，无法清理文章文件");
            }
            if (storage instanceof LocalStorage) {
                String localRoot = ((LocalStorage) storage).getLocalFilePath();
                SitePageUtil.checkLocalPath(localRoot, storagePath);
                Path target = new File(localRoot + storagePath).toPath().toAbsolutePath().normalize();
                BasicFileAttributes attributes;
                try {
                    attributes = Files.readAttributes(target, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
                } catch (NoSuchFileException e) {
                    return BaseVO.success();
                }
                if (!attributes.isRegularFile()) {
                    return BaseVO.failure("目标是目录或其他非普通文件；文章文件清理仅允许删除单个普通文件");
                }
                Files.deleteIfExists(target);
                return BaseVO.success();
            }
            com.xnx3.BaseVO result = storage.delete(storagePath);
            if (result == null) {
                return BaseVO.failure("存储删除接口未返回执行结果（null）");
            }
            if (result.getResult() != com.xnx3.BaseVO.SUCCESS) {
                String reason = result.getInfo();
                if (reason == null || reason.trim().length() == 0) {
                    reason = "存储删除接口返回状态 " + result.getResult() + "，但未提供失败原因";
                }
                return BaseVO.failure(reason);
            }
            return BaseVO.success();
        } catch (Exception e) {
            String reason = e.getMessage();
            return BaseVO.failure(reason == null || reason.length() == 0 ? e.getClass().getSimpleName() : reason);
        }
    }
}
