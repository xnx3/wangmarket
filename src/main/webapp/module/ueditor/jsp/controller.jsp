<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.xnx3.wangmarket.admin.util.UEditorActionEnter"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%

	request.setCharacterEncoding( "utf-8" );
	response.setContentType(request.getParameter("callback") == null
			? "application/json; charset=UTF-8" : "application/javascript; charset=UTF-8");
	response.setHeader("X-Content-Type-Options", "nosniff");
	
	String rootPath = application.getRealPath( "/" );
	
	out.write( new UEditorActionEnter( request, rootPath ).exec() );
	
%>
