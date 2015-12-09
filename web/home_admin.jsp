<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<c:if test="${empty current_lang}">
    <c:set var="current_lang" value="en_US"/>
</c:if>

<fmt:setLocale value="${current_lang}" scope="session"/>
<fmt:setBundle basename="resources/content" var="lang" scope="session"/>

<head>
    <title><fmt:message key="home.title" bundle="${lang}"/></title>
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
    <input type="hidden" name="current_page" value="home_admin.jsp"/>
    <input type="hidden" name="command" value="language">
    <input type="hidden" name="sub_command" value="load_products"/>
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
                    <input type="submit" class="menu_butt selected" value=<fmt:message key="menubar.home" bundle="${lang}"/>>
                </form></li>

                <li>
                    <form action="profile" method="get">
                        <input type="hidden" name="sub_command" value="load_unpaid_orders"/>
                        <input type="hidden" name="current_page" value="orders.jsp"/>
                        <input type="submit" class="menu_butt" value="<fmt:message key="orders.button" bundle="${lang}"/>">
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
            <h1><fmt:message key="home.welcome" bundle="${lang}"/></h1>
            <table align="center">
                <tr>
                    <th><fmt:message key="table.id" bundle="${lang}"/></th>
                    <th>image</th>
                    <th><fmt:message key="table.Name" bundle="${lang}"/></th>
                    <th><fmt:message key="table.description" bundle="${lang}"/></th>
                    <th><fmt:message key="table.price" bundle="${lang}"/></th>
                </tr>
                <c:forEach var="product" items="${products}" >
                    <tr>
                        <td>${product.id}</td>
                        <td><img src="${product.imagePath}" style="width: 130px; height: 200px"></td>
                        <td>${product.name}</td>
                        <td style="text-align: justify">${product.description}</td>
                        <td>${product.price}$</td>
                        <td>
                            <form action="profile" enctype="multipart/form-data" method="get">
                                <input type="hidden" name="sub_command" value="find_product_to_edit"/>
                                <input type="hidden" name="current_page" value="editproduct.jsp"/>
                                <input type="hidden" name="id_product" value="${product.id}">
                                <input type="submit" class="login login-submit" value=<fmt:message key="edit.button" bundle="${lang}"/>>
                            </form>
                            <form action="controller" enctype="multipart/form-data" method="post">
                                <input type="hidden" name="command" value="remove_product"/>
                                <input type="hidden" name="page_number" value="${page_number}">
                                <input type="hidden" name="id_product" value="${product.id}">
                                <input type="submit" class="login login-submit" value=<fmt:message key="remove.button" bundle="${lang}"/>>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </table>

            <div>
                <c:forEach begin="1" end="${count_pages}" var="i">
                    <c:choose>
                        <c:when test="${page_number eq i}">
                            <form style="display: inline-block">
                                <input class="login pagination pagination-submit selected" disabled type="text" name="page_number" value="${i}"/>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <form style="display: inline-block" action="controller" enctype="multipart/form-data" method="post">
                                <input type="hidden" name="command" value="load_products"/>
                                <input type="hidden" name="page_number" value="${i}"/>
                                <input class="login pagination pagination-submit" type="submit" value="${i}"/>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
        </div>
    </div>
    <div id="content_footer"></div>
    <div id="footer">
        <p><fmt:message key="home.copyright" bundle="${lang}"/> &copy; OrangeProject</p>
    </div>
</div>
</body>
</html>
