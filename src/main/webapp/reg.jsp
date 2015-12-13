<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: stas-
  Date: 11/5/2015
  Time: 1:39 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html >

<c:if test="${empty current_lang}">
    <c:set var="current_lang" value="en_US"/>
</c:if>

<fmt:setLocale value="${current_lang}" scope="session"/>
<fmt:setBundle basename="content" var="lang" scope="session"/>

<head>
    <meta charset="UTF-8">
    <title><fmt:message key="registration.title" bundle="${lang}"/></title>
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
    <input type="hidden" name="controller_page" value="reg.jsp">
    <input type="hidden" name="command" value="language">
    <input type="submit" style="position: absolute; left: -9999px" name="dropdown">

</form>

<div class="login-card">
    <h1 class="log"><fmt:message key="registration.title" bundle="${lang}"/> </h1><br>
    <form action="controller" enctype="multipart/form-data" method="post">
        <input type="hidden" name="command" value="register"/>
        <input type="text" name="user_name" required placeholder=<fmt:message key="username.placeholder" bundle="${lang}"/>>
        <input type="password" name="password" required placeholder=<fmt:message key="password.placeholder" bundle="${lang}"/>>
        <input type="text" name="first_name" required placeholder="<fmt:message key="first_name" bundle="${lang}"/>">
        <input type="text" name="last_name" required placeholder="<fmt:message key="sec_name" bundle="${lang}"/>">
        <input type="submit" class="login login-submit" value=<fmt:message key="reg.button" bundle="${lang}"/>>
    </form>
    <div class="login-help">
        <a href="index.jsp"><fmt:message key="backtologin.button" bundle="${lang}"/></a>
    </div>
    <c:if test="${not empty param['message']}">
        <p class="login-message"><fmt:message key="message.userexist" bundle="${lang}"/></p>
    </c:if>

</div>

<!-- <div id="error"><img src="https://dl.dropboxusercontent.com/u/23299152/Delete-icon.png" /> Your caps-lock is on.</div> -->
<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
<script src='http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js'></script>
