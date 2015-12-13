<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<c:if test="${empty current_lang}">
    <c:set var="current_lang" value="en_US"/>
</c:if>

<fmt:setLocale value="${current_lang}" scope="session"/>
<fmt:setBundle basename="content" var="lang" scope="session"/>

<head>
    <title><fmt:message key="orders.title" bundle="${lang}"/></title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<form id="langform" name="langform" action="controller" enctype="multipart/form-data" method="post">
    <p style="padding-bottom: 0px">
        <select name="lang" form="langform" onchange="document.langform.submit();">
            <option selected disabled hidden value=''>${current_lang}</option>
            <option value="en_US">English</option>
            <option value="ru_RU">Русский</option>
        </select>
    </p>
    <input type="hidden" name="controller_page" value="profile">
    <input type="hidden" name="current_page" value="orders.jsp"/>
    <input type="hidden" name="command" value="language">
    <input type="hidden" name="sub_command" value="load_unpaid_orders"/>
    <input type="submit" style="position: absolute; left: -9999px" name="dropdown">
</form>

<div id="main">
    <div id="header">
        <div id="logo">
            <div id="logo_text">
                <!-- class="logo_colour", allows you to change the colour of the text -->
                <h1><a><fmt:message key="header.firsttext" bundle="${lang}"/><span class="logo_colour"><fmt:message key="header.secondtext" bundle="${lang}"/></span></a></h1>
                <h2><fmt:message key="subheader" bundle="${lang}"/></h2>
            </div>
        </div>
        <div id="menubar">
            <ul id="menu">
                <li><form action="profile" method="get">
                    <input type="submit" class="menu_butt" value=<fmt:message key="menubar.home" bundle="${lang}"/>>
                </form></li>

                <li>
                    <form action="profile" method="get">
                        <input type="hidden" name="sub_command" value="load_unpaid_orders"/>
                        <input type="hidden" name="current_page" value="orders.jsp"/>
                        <input type="submit" class="menu_butt selected" value="<fmt:message key="orders.button" bundle="${lang}"/>">
                    </form>
                </li>

                <li>
                    <form action="editproduct.jsp">
                        <input type="submit" class="menu_butt" value="<fmt:message key="addnewproduct.button" bundle="${lang}"/>">
                    </form>
                </li>

                <li>
                    <form action="controller" enctype="multipart/form-data" method="get">
                        <input type="hidden" name="command" value="logout"/>
                        <input type="submit" class="menu_butt" value=<fmt:message key="menubar.logout" bundle="${lang}"/>>
                    </form>
                </li>
            </ul>
        </div>
    </div>
    <div id="content_header"></div>
    <div id="site_content">
        <div id="content">
            <c:if test="${not empty unpaid_orders}">
                <table align="center">
                    <tr>
                        <th><fmt:message key="table.idOrder" bundle="${lang}"/></th>
                        <th><fmt:message key="table.idCustomer" bundle="${lang}"/></th>
                        <th><fmt:message key="table.Date" bundle="${lang}"/></th>
                        <th></th>
                    </tr>
                    <c:forEach var="order" items="${unpaid_orders}">
                        <tr>
                            <td>${order.idOrder}</td>
                            <td>${order.idCustomer}</td>
                            <td>${order.date}</td>
                            <td>
                                <form action="controller" enctype="multipart/form-data" method="post">
                                    <input type="hidden" name="command" value="ban_user"/>
                                    <input type="hidden" name="user_id" value="${order.idCustomer}">
                                    <input type="submit" class="login login-submit" value="<fmt:message key="ban.button" bundle="${lang}"/>">
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>

            <c:if test="${empty unpaid_orders}">
                <h1><fmt:message key="emptyorders" bundle="${lang}"/></h1>
            </c:if>
        </div>
    </div>
    <div id="content_footer"></div>
    <div id="footer">
        <p><fmt:message key="home.copyright" bundle="${lang}"/> &copy; OrangeProject</p>
    </div>
</div>
</body>
</html>
