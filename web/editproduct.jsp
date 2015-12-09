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
    <title><fmt:message key="editproduct.title" bundle="${lang}"/></title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

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
                        <input type="submit" class="menu_butt" value="<fmt:message key="orders.button" bundle="${lang}"/>">
                    </form>
                </li>

                <li><form action="editproduct.jsp">
                    <input type="submit" class="<c:if test="${empty product}">selected </c:if> menu_butt" value="<fmt:message key="addnewproduct.button" bundle="${lang}"/>">
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
            <h1><fmt:message key="edit.welcome" bundle="${lang}"/></h1>
            <div class="login-card edit-card">
                <form action="controller" enctype="multipart/form-data" accept-charset="UTF-8" method="post">
                    <c:if test="${not empty product}">
                        <input type="hidden" name="command" value="edit_product"/>
                        <h2><fmt:message key="table.id" bundle="${lang}"/>:</h2>
                        <input type="text" readonly name="id_product" value="${product.id}" placeholder=<fmt:message key="table.id" bundle="${lang}"/>>
                    </c:if>
                    <c:if test="${empty product}">
                        <input type="hidden" name="command" value="add_product"/>
                    </c:if>
                    <h2><fmt:message key="table.Name" bundle="${lang}"/>:</h2>
                    <input type="text" name="product_name" required value="${product.name}" placeholder=<fmt:message key="table.Name" bundle="${lang}"/>>
                    <h2><fmt:message key="table.description" bundle="${lang}"/>:</h2>
                    <textarea rows="10" name="product_description" required placeholder="<fmt:message key="table.description" bundle="${lang}"/>">${product.description}</textarea>
                    <h2><fmt:message key="table.price" bundle="${lang}"/>:</h2>
                    <input type="number" min="0" step="any" name="product_price" required value="${product.price}" placeholder=<fmt:message key="table.price" bundle="${lang}"/>>
                    <h2><fmt:message key="table.image" bundle="${lang}"/>:</h2>
                    <c:if test="${not empty product.imagePath}">
                        <input type="hidden" name="old_image" value="${product.imagePath}">
                        <img src="${product.imagePath}" style="width: 130px; height: 200px">
                        <br>
                    </c:if>
                    <input type="file" name="image" accept="image/jpeg">
                    <input type="submit" class="login login-submit" value=<fmt:message key="apply.button" bundle="${lang}"/>>
                </form>
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
