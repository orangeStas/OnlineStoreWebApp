<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="mytag" uri="/web-inf/tld/taglib.tld"%>
<html>

<c:if test="${empty current_lang}">
    <c:set var="current_lang" value="en_US"/>
</c:if>

<fmt:setLocale value="${current_lang}" scope="session"/>
<fmt:setBundle basename="content" var="lang" scope="session"/>

<head>
    <title><fmt:message key="order.title" bundle="${lang}"/></title>
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
    <input type="hidden" name="current_page" value="confirm.jsp"/>
    <input type="hidden" name="command" value="language">
    <input type="hidden" name="sub_command" value="receive_order"/>
    <input type="submit" style="position: absolute; left: -9999px" name="dropdown">
</form>

<div id="main">
    <div id="header">
        <div id="logo">
            <div id="logo_text">
                <h1><a><fmt:message key="header.firsttext" bundle="${lang}"/><span class="logo_colour"><fmt:message key="header.secondtext" bundle="${lang}"/></span></a></h1>
                <h2><fmt:message key="subheader" bundle="${lang}"/></h2>
            </div>
        </div>
        <div id="menubar">
            <ul id="menu">
                <li><form action="profile" method="get">
                    <input type="submit" class="menu_butt" value=<fmt:message key="menubar.home" bundle="${lang}"/>>
                </form></li>

                <li><form action="#" method="get">
                    <input type="submit" class="menu_butt selected" value=<fmt:message key="menubar.order" bundle="${lang}"/>>
                </form></li>

                <li><form action="controller" enctype="multipart/form-data" method="get">
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
            <c:if test="${not empty order}">
                <h1><fmt:message key="order.id" bundle="${lang}"/> ${order.idOrder}</h1>
                <h1><fmt:message key="order.date" bundle="${lang}"/> ${order.date} </h1>
                <table align="center">
                    <tr>
                        <th><th><fmt:message key="table.image" bundle="${lang}"/></th></th>
                        <th><fmt:message key="table.Name" bundle="${lang}"/></th>
                        <th><fmt:message key="table.description" bundle="${lang}"/></th>
                        <th colspan="2"><fmt:message key="table.price" bundle="${lang}"/></th>
                    </tr>
                    <c:forEach var="product" items="${products}" >
                        <tr>
                            <td><img src="${product.imagePath}" style="width: 130px; height: 200px"></td>
                            <td>${product.name}</td>
                            <td style="text-align: justify">${product.description}</td>
                            <td>${product.price}$</td>
                            <td>
                                <form action="controller" enctype="multipart/form-data" method="post">
                                    <input type="hidden" name="command" value="remove_product_from_order"/>
                                    <input type="hidden" name="sub_command" value="receive_order"/>
                                    <input type="hidden" name="current_page" value="confirm.jsp"/>
                                    <input type="hidden" name="id_product" value="${product.id}">
                                    <input type="submit" class="login login-submit" value=<fmt:message key="remove.button" bundle="${lang}"/>>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <div class="confirmline">
                    <h1 style="position: absolute; left: 0; top: 0"><fmt:message key="order.totalcost" bundle="${lang}"/>
                        <mytag:formatCurrency totalCostValue="${order.totalCost}" currency="EUR"/>
                    </h1>
                    <form action="controller" enctype="multipart/form-data" method="post">
                        <input type="hidden" name="command" value="confirm_order"/>
                        <input type="submit" class="login confirm-submit" value=<fmt:message key="order.confirm" bundle="${lang}"/>>
                    </form>
                </div>
            </c:if>
            <c:if test="${empty order}">
                <h1><fmt:message key="order.emptyCart" bundle="${lang}"/></h1>
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
