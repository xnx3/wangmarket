package com.xnx3.j2ee.system;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * 错误页配置，如404、500错误
 * @author 管雷鸣
 *
 */
@Component
public class ErrorConfigurar implements ErrorPageRegistrar{

	public void registerErrorPages(ErrorPageRegistry registry) {
		ErrorPage[] errorPages=new ErrorPage[3];
        errorPages[0]=new ErrorPage(HttpStatus.NOT_FOUND,"/404.do");
        errorPages[1]=new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,"/500.do");
        //errorPages[2]=new ErrorPage(HttpStatus.NOT_ACCEPTABLE,"/406.do");
        //errorPages[3]=new ErrorPage(org.apache.shiro.authz.AuthorizationException.class, "/403.do");	//优先根据此来进行排查。 先根据具体异常的类、再根据错误码
        errorPages[2]=new ErrorPage(org.springframework.web.multipart.MaxUploadSizeExceededException.class, "/406.do");	//
        
        
        registry.addErrorPages(errorPages);
	}
	
}
