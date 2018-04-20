package com.xnx3.wangmarket.markdown.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xnx3.j2ee.func.AttachmentFile;
import com.xnx3.j2ee.vo.UploadFileVO;
import com.xnx3.wangmarket.admin.controller.BaseController;
import com.xnx3.wangmarket.admin.util.AliyunLog;

/**
 * 模版相关操作
 * @author 管雷鸣
 */
@Controller
@RequestMapping("/markdownPlugin")
public class MarkDownEditPluginController extends BaseController {
	
	/**
	 * 上传图片接口
	 */
	@RequestMapping("uploadImage${url.suffix}")
	public void uploadImage(Model model,HttpServletRequest request, HttpServletResponse response){
		JSONObject json = new JSONObject();
		UploadFileVO uploadFileVO = new UploadFileVO();
		if(getSite() == null){
			json.put("success", 0);
			json.put("success", "请先登录");
		}else{
			uploadFileVO = AttachmentFile.uploadImage("site/"+getSiteId()+"/news/", request, "editormd-image-file", 0);
			if(uploadFileVO.getResult() == UploadFileVO.SUCCESS){
				json.put("success", 1);
				json.put("message", "上传成功");
				json.put("url", uploadFileVO.getUrl());
				//上传成功，写日志
				AliyunLog.addActionLog(getSiteId(), "CMS模式下，模版页自由上传图片成功："+uploadFileVO.getFileName());
			}else{
				json.put("success", 0);
				json.put("message", uploadFileVO.getInfo());
			}
		}
		
		response.setCharacterEncoding("UTF-8");  
	    response.setContentType("text/html; charset=utf-8");  
	    PrintWriter out = null;  
	    try {  
	        out = response.getWriter();  
	        out.append(json.toString());
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } finally {  
	        if (out != null) {  
	            out.close();  
	        }  
	    }
	}
	

	
}