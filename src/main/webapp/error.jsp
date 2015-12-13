<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: stas-
  Date: 10/28/2015
  Time: 1:10 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <title>Ooops</title>

    <link href="https://fonts.googleapis.com/css?family=Lato:100" rel="stylesheet" type="text/css">

    <style>
        html, body {
            height: 100%;
        }

        body {
            margin: 0;
            padding: 0;
            width: 100%;
            background-color: #3498DB ;
            color: #fdfdfe;
            display: table;
            font-weight: 100;
            font-family: 'Lato';
            font-size: 14px;
        }

        .container {
            text-align: center;
            display: table-cell;
            vertical-align: middle;
        }

        .content {
            text-align: center;
            display: inline-block;
        }

        .title {
            font-size: 72px;
            margin-bottom: 40px;
        }

        table {
            font-size: 20px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="content">
        <div class="title">Something going wrong :(</div>
        <table width="100%" border="1">
            <tr valign="top">
                <td width="40%"><b>Error:</b></td>
                <td>${pageContext.exception}</td>
            </tr>
            <tr valign="top">
                <td><b>URI:</b></td>
                <td>${pageContext.errorData.requestURI}</td>
            </tr>
            <tr valign="top">
                <td><b>Status code:</b></td>
                <td>${pageContext.errorData.statusCode}</td>
            </tr>
            <tr valign="top">
                <td><b>Stack trace:</b></td>
                <td>
                    <c:forEach var="trace"
                               items="${pageContext.exception.stackTrace}">
                        <p>${trace}</p>
                    </c:forEach>
                </td>
            </tr>
        </table>
    </div>
</div>
</body>
</html>
