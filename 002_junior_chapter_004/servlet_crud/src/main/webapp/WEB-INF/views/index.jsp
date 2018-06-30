<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="context" value="${pageContext.request.contextPath}"/>
<c:set var="list" value="list"/>

<html>
<head>
    <title>User store</title>
</head>
<body>

<div align="center">
    <h1>User store</h1>
</div>

<table align="center">
    <tr>
        <td valign="center">
            <form action="${context}/${list}">
                <input type="hidden" name="store" value="collection"/>
                <input type="submit" value="Collection"/>
            </form>
        </td>
        <td valign="center">
            <form action="${context}/${list}">
                <input type="hidden" name="store" value="database"/>
                <input type="submit" value="Database"/>
            </form>
        </td>
    </tr>
</table>
</body>
</html>
