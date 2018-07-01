<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="dateTime" class="java.util.Date"/>
<c:set var="context" value="${pageContext.request.contextPath}"/>
<c:set var="store" value="${param.store}"/>
<c:set var="create" value="create"/>
<c:set var="update" value="update"/>
<c:set var="delete" value="list"/>

<html>
<head>
    <title>User list: ${store}</title>
</head>
<body>

<p align="center">
    <a href="<c:url value="${context}"/>">Index page</a>
</p>

<form action="${context}/${create}" method="get">
    <p align="center">
        <input type="hidden" name="store" value="${store}"/>
        <input type="submit" value="create user"/>
    </p>
</form>

<div align="center">
    <h1>Users list: ${store}</h1>
</div>

<table style="border: 1px solid black;" cellpadding="5px" cellspacing="0px" border="1px" align="center" valign="center">
    <tr align="center" valign="center">
        <th>id</th>
        <th>name</th>
        <th>login</th>
        <th>email</th>
        <th>created</th>
        <th colspan="2">Actions</th>
    </tr>

    <jsp:useBean id="users" scope="request" type="java.util.List"/>
    <c:forEach items="${users}" var="user">
        <tr align="center" valign="center">
            <td><c:out value="${user.id}"/>
            </td>
            <td><c:out value="${user.name}"/>
            </td>
            <td><c:out value="${user.login}"/>
            </td>
            <td><c:out value="${user.email}"/>
            </td>
            <td>
                <jsp:setProperty name="dateTime" property="time" value="${user.created}"/>
                <fmt:formatDate value="${dateTime}" pattern="dd.mm.yyyy HH:mm:ss"/>
            </td>
            <td>
                <form action="${update}" method="get">
                    <input type="hidden" name="store" value="${store}"/>
                    <input type="hidden" name="id" value="${user.id}">
                    <input type="submit" value="update">
                </form>
            </td>
            <td>
                <form action="${delete}" method="post">
                    <input type="hidden" name="store" value="${store}"/>
                    <input type="hidden" name="id" value="${user.id}">
                    <input type="submit" value="delete">
                </form>
            </td>
        </tr>
    </c:forEach>

</table>
</body>
</html>
