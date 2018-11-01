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
        errorPages[2]=new ErrorPage(HttpStatus.NOT_ACCEPTABLE,"/406.do");

        registry.addErrorPages(errorPages);
	}
	
}
