package com.xnx3.wangmarket.admin.init;

import org.springframework.stereotype.Component;

import com.xnx3.j2ee.util.ConsoleUtil;
import com.xnx3.j2ee.util.SpringUtil;
import com.xnx3.j2ee.util.SystemUtil;
import com.xnx3.wangmarket.admin.entity.News;
import com.xnx3.wangmarket.admin.entity.SiteColumn;

/**
 * 修正历史上误写到 news.type 中的栏目类型值。
 * 旧代码会把 SiteColumn.TYPE_LIST / TYPE_ALONEPAGE 写入 News.type，
 * 导致后续按 News.TYPE_* 判断的逻辑出现偏差。
 */
@Component
public class NormalizeNewsTypeData {

	public NormalizeNewsTypeData() {
		new Thread(new Runnable() {
			public void run() {
				ConsoleUtil.info("start normalize news.type data thread.");
				while (SystemUtil.get("ALLOW_USER_REG") == null) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				try {
					int listToNews = SpringUtil.getSqlService().executeSql(
							"UPDATE news SET type = " + News.TYPE_NEWS + " WHERE type = " + SiteColumn.TYPE_LIST);
					int alonePageToPage = SpringUtil.getSqlService().executeSql(
							"UPDATE news SET type = " + News.TYPE_PAGE + " WHERE type = " + SiteColumn.TYPE_ALONEPAGE);
					ConsoleUtil.info("NormalizeNewsTypeData Finish ! repair list->news: " + listToNews
							+ ", alonePage->page: " + alonePageToPage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
