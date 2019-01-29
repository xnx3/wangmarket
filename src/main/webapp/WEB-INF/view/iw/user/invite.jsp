<%@page import="com.xnx3.j2ee.Global"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<jsp:include page="../common/head.jsp">
	<jsp:param name="title" value="邀请注册"/>
</jsp:include>

					您的邀请注册地址：
					<br/>
					/reg.do?inviteid=${user.id }
					<br/>
					邀请他人注册，可以获得丰厚的<%=Global.get("CURRENCY_NAME") %>奖励！
					<br/>比如：
					<br/>
					<br/>我推荐了B用户注册后，我单纯的推荐注册收益如下： 
					<br/>
					<br/>我推荐B注册，我获得<%=Global.get("INVITEREG_AWARD_ONE") %><%=Global.get("CURRENCY_NAME") %> 
					<br/>B又推荐C注册，B用户获得<%=Global.get("INVITEREG_AWARD_ONE") %><%=Global.get("CURRENCY_NAME") %>，我获得<%=Global.get("INVITEREG_AWARD_TWO") %><%=Global.get("CURRENCY_NAME") %>
					<br/>C又推荐D注册，C用户获得<%=Global.get("INVITEREG_AWARD_ONE") %><%=Global.get("CURRENCY_NAME") %>，B用户获得<%=Global.get("INVITEREG_AWARD_TWO") %><%=Global.get("CURRENCY_NAME") %>，我获得<%=Global.get("INVITEREG_AWARD_THREE") %><%=Global.get("CURRENCY_NAME") %> 
					<br/>D又推荐E注册，D用户获得<%=Global.get("INVITEREG_AWARD_ONE") %><%=Global.get("CURRENCY_NAME") %>，C用户获得<%=Global.get("INVITEREG_AWARD_TWO") %><%=Global.get("CURRENCY_NAME") %>，B用户获得<%=Global.get("INVITEREG_AWARD_THREE") %><%=Global.get("CURRENCY_NAME") %>，我获得<%=Global.get("INVITEREG_AWARD_FOUR") %><%=Global.get("CURRENCY_NAME") %>
					
				</div>
