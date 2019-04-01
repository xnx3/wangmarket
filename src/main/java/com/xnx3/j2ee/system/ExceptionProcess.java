package com.xnx3.j2ee.system;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import com.xnx3.j2ee.func.AttachmentFile;
import com.xnx3.j2ee.vo.BaseVO;

//@ControllerAdvice
public class ExceptionProcess {
    
	// 对这个异常的统一处理，返回值 和Controller的返回规则一样
//    @ExceptionHandler(org.springframework.web.multipart.MaxUploadSizeExceededException.class)
//    @ResponseBody
//    @ResponseStatus(HttpStatus.OK)  
    public BaseVO maxUploadSizeExceededException(MaxUploadSizeExceededException e){
    	BaseVO vo = new BaseVO();
    	vo.setBaseVO(BaseVO.FAILURE, "--请上传大小在 "+ AttachmentFile.getMaxFileSize()+" 之内的文件");
    	System.out.println("--ExceptionProcess - MaxUploadSizeExceededException : "+e.getMessage());
    	return vo;
    }
}