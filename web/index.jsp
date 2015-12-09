<%--
  Created by IntelliJ IDEA.
  User: stas-
  Date: 11/3/2015
  Time: 9:04 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html >

<c:if test="${empty current_lang}">
    <c:set var="current_lang" value="en_US"/>
</c:if>

<fmt:setLocale value="${current_lang}" scope="session"/>
<fmt:setBundle basename="resources/content" var="lang" scope="session"/>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="login.title" bundle="${lang}"/></title>
    <link rel='stylesheet prefetch' href='http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/themes/smoothness/jquery-ui.css'>
    <link rel="stylesheet" href="css/style.css">
</head>

<body>

<form id="langform" name="langform" action="controller" enctype="multipart/form-data" method="post">
    <p>
        <select name="lang" form="langform" onchange="document.langform.submit();">
            <option selected disabled hidden value=''>${current_lang}</option>
            <option value="en_US">English</option>
            <option value="ru_RU">Русский</option>
        </select>
    </p>
    <input type="hidden" name="controller_page" value="index.jsp">
    <input type="hidden" name="command" value="language">
    <input type="submit" style="position: absolute; left: -9999px" name="dropdown">
</form>

<div class="login-card">
    <h1 class="log"><fmt:message key="login.title" bundle="${lang}"/></h1><br>
    <form action="controller" enctype="multipart/form-data" method="post">
        <input type="hidden" name="command" value="login"/>
        <input type="text" name="user_name" required placeholder=<fmt:message key="username.placeholder" bundle="${lang}"/>>
        <input type="password" name="password" required placeholder=<fmt:message key="password.placeholder" bundle="${lang}"/>>
        <input type="submit" class="login login-submit" value=<fmt:message key="login.button" bundle="${lang}"/>>
    </form>

    <div class="login-help">
        <a href="reg.jsp"><fmt:message key="register.button" bundle="${lang}"/></a>
    </div>
    <c:if test="${not empty param['message']}">
        <c:choose>
            <c:when test="${param['message'] == 'ban'}">
                <p class="login-message"><fmt:message key="message.ban" bundle="${lang}"/></p>
            </c:when>
            <c:when test="${param['message'] == 'incorrect'}">
                <p class="login-message"><fmt:message key="message.incorrect" bundle="${lang}"/></p>
            </c:when>
        </c:choose>

    </c:if>

</div>

<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
<script src='http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js'></script>
