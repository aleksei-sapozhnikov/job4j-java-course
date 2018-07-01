<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="context" value="${pageContext.request.contextPath}"/>
<c:set var="store" value="${param.store}"/>
<c:set var="list" value="list"/>
<c:set var="login" value="login"/>
<%--@elvariable id="error" type="java.lang.String"--%>
<c:set var="error" value="${error}"/>

<html>

<head>
    <title>Login : ${store}</title>
</head>

<body>

<div align="center">
    <h1>Login: ${store}</h1>
</div>

<c:if test="${error != ''}">
    <div style="background-color: red">
        <c:out value="${error}"/>
    </div>
</c:if>

<form action="<c:url value="/${login}"/>" method="post">
    <table align="center">
        <tr>
            <td align="left" valign="center">Login:</td>
            <td><input type="text" name="login"/></td>
        </tr>
        <tr>
            <td align="left" valign="center">Password:</td>
            <td><input type="password" name="password"/></td>
        </tr>
        <tr>
            <td align="left" valign="center" colspan="2">
                <input type="hidden" name="store" value="${store}"/>
                <input type="submit" value="login"/>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
