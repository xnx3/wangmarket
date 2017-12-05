package com.xnx3.admin.controller.client;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.aliyun.oss.model.OSSObjectSummary;
import com.xnx3.j2ee.service.SqlService;
import com.xnx3.net.OSSUtil;
import com.xnx3.admin.controller.BaseController;
import com.xnx3.admin.entity.Site;
import com.xnx3.admin.service.SiteService;
import com.xnx3.admin.vo.SiteFileListVO;
import com.xnx3.admin.vo.bean.OSSFile;

/**
 * 公共的
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/site")
public class ClientSiteController extends BaseController {
	@Resource
	private SqlService sqlService;
	@Resource
	private SiteService siteService;

	/**
	 * 获取站点的文件列表
	 */
	@RequestMapping("siteFileList")
	@ResponseBody
	public SiteFileListVO siteFileList(){
		SiteFileListVO vo = new SiteFileListVO();
		
		//获取用户的站点
		Site site = sqlService.findAloneBySqlQuery("SELECT * FROM site WHERE userid = "+getUserId()+" ORDER BY id DESC", Site.class);
		if(site == null){
			vo.setBaseVO(SiteFileListVO.FAILURE, "您尚未创建站点");
			return vo;
		}
		
		List<OSSObjectSummary> list = OSSUtil.getFolderObjectList("site/"+site.getId()+"/");
		List<OSSFile> ossFileList = new ArrayList<OSSFile>();
		for (int i = 0; i < list.size(); i++) {
			OSSObjectSummary os = list.get(i);
			ossFileList.add(new OSSFile(os.getKey(), os.getSize()));
		}
		vo.setList(ossFileList);
		vo.setSite(site);
		
		return vo;
	}
	
}
