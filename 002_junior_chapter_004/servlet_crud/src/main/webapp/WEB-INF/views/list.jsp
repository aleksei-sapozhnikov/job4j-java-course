<jsp:useBean id="user" scope="request" type="ru.job4j.crud.User"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="dateTime" scope="request" class="java.util.Date"/>
<c:set var="context" value="${pageContext.request.contextPath}"/>
<c:set var="create" value="create"/>
<c:set var="update" value="update"/>
<c:set var="delete" value="delete"/>

<html>
<head>
    <title>User list</title>
</head>
<body>

<c:if test="${error != ''}">
    <div style="background-color: red" align="center">
        <span style="color: white; ">
            <c:out value="${error}"/>
        </span>
    </div>
    <br>
</c:if>

<c:if test="${param.errorString != null}">
    <div style="background-color: red" align="center">
        <span style="color: white; ">
            <c:out value="${param.errorString}"/>
        </span>
    </div>
    <br>
</c:if>

<div align="center">
    logged: id=${user.id}, name=${user.name}, role=${user.role}
</div>

<form action="<c:url value="/logout"/>" method="post">
    <div align="center">
        <input type="submit" value="logout">
    </div>
</form>

<form action="<c:url value="/create"/>" method="get">
    <p align="center">
        <input type="submit" value="create user"/>
    </p>
</form>

<div align="center">
    <h1>Users list</h1>
</div>

<table style="border: 1px solid black;" cellpadding="5px" cellspacing="0px" border="1px" align="center" valign="center">
    <tr align="center" valign="center">
        <th>id</th>
        <th>name</th>
        <th>login</th>
        <th>email</th>
        <th>role</th>
        <th>created</th>
        <th colspan="2">actions</th>
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
            <td><c:out value="${user.role}"/>
            </td>
            <td>
                <jsp:setProperty name="dateTime" property="time" value="${user.created}"/>
                <fmt:formatDate value="${dateTime}" pattern="dd.MM.yyyy HH:mm:ss"/>
            </td>
            <td>
                <form action="${update}" method="get">
                    <input type="hidden" name="id" value="${user.id}">
                    <input type="submit" value="update">
                </form>
            </td>
            <td>
                <form action="${delete}" method="post">
                    <input type="hidden" name="id" value="${user.id}">
                    <input type="submit" value="delete">
                </form>
            </td>
        </tr>
    </c:forEach>

</table>
</body>
</html>
