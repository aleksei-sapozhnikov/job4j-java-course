<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="context" value="${pageContext.request.contextPath}"/>
<c:set var="store" value="${param.store}"/>
<c:set var="create" value="create"/>

<html>
<head>
    <title>Create user: ${store}</title>
</head>
<body>

<div align="center">
    <h1>Create user: ${store}</h1>
</div>

<form action="${context}/${create}" method="post">
    <table style="border: 0" align="center">
        <tr>
            <td align="left">User name:</td>
            <td><input type="text" name="name"></td>
        </tr>
        <tr>
            <td align="left">User login:</td>
            <td><input type="text" name="login"></td>
        </tr>
        <tr>
            <td align="left">User email:</td>
            <td><input type="text" name="email"></td>
        </tr>
        <tr>
            <td align="center" valign="center">
                <input type="hidden" name="store" value="${store}"/>
                <input type="submit" value="create">
            </td>
        </tr>
    </table>
</form>
</body>
</html>
