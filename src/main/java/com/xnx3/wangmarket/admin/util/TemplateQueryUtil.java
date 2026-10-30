package com.xnx3.wangmarket.admin.util;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import com.xnx3.wangmarket.admin.entity.Site;

/** 模板查询统一绑定当前站点和模板编码。 */
public final class TemplateQueryUtil {
    private TemplateQueryUtil() { }

    public static Query query(EntityManager manager, String sql, Class<?> type, Site site) {
        Query query = type == null ? manager.createNativeQuery(sql) : manager.createNativeQuery(sql, type);
        query.setParameter("siteid", site.getId());
        if (sql.contains(":templateName")) {
            query.setParameter("templateName", site.getTemplateName());
        }
        return query;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> list(EntityManager manager, String sql, Class<T> type, Site site) {
        return query(manager, sql, type, site).getResultList();
    }

    public static <T> T first(EntityManager manager, String sql, Class<T> type, Site site,
            String parameter, Object value) {
        List<?> results = query(manager, sql, type, site).setParameter(parameter, value).setMaxResults(1).getResultList();
        return results.isEmpty() ? null : type.cast(results.get(0));
    }
}
