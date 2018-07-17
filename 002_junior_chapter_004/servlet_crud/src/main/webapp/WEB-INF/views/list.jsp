<jsp:useBean id="user" scope="request" type="ru.job4j.crud.User"/>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="dateTime" scope="request" class="java.util.Date"/>
<c:set var="context" value="${pageContext.request.contextPath}"/>
<c:set var="create" value="create"/>
<c:set var="update" value="update"/>
<c:set var="delete" value="delete"/>
<c:set var="logout" value="logout"/>
<c:set var="create" value="create"/>

<html>
<head>
    <title>User list</title>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</head>
<body>

<nav class="navbar navbar">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">Main page</a>
        </div>
        <ul class="nav navbar-nav">
            <li><a href="${context}/${create}">Create user</a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li>
                <a href="${context}/">
                    <span class="glyphicon glyphicon-user"></span>
                    Welcome, ${user.name} (id=${user.id}, ${user.role})
                </a>
            </li>
            <li>
                <form action="${context}/${logout}" method="post" class="inline">
                    <span class="glyphicon glyphicon-log-out"></span>
                    <button type="submit" class="link-button">
                        Exit
                    </button>
                </form>
            </li>
        </ul>
    </div>
</nav>

<form action="results.php" method="POST" role="form" class="form-horizontal">
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default">Search</button>
        </div>
    </div>
</form>


<div align="center">
    logged: id=${user.id}, name=${user.name}, role=${user.role}
</div>

<%--@elvariable id="error" type="java.lang.String"--%>
<c:if test="${error != null}">
    <div class="text-center">
        <div class="alert alert-danger">
            <strong>Error!</strong> ${error}
        </div>
    </div>
</c:if>

<c:if test="${param.error != null}">
    <div class="text-center">
        <div class="alert alert-danger">
            <strong>Error!</strong> ${param.error}
        </div>
    </div>
</c:if>

<form action="<c:url value="${logout}"/>" method="post">
    <div align="center">
        <input type="submit" value="logout">
    </div>
</form>

<form action="<c:url value="${context}/${create}"/>" method="get">
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
