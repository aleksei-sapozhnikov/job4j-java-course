<%@ page contentType="text/html;charset=UTF-8" %>

<!-- JSP libraries -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- Paths -->
<c:set var="context" value="${pageContext.request.contextPath}"/>
<c:set var="create" value="/create"/>
<c:set var="update" value="/update"/>
<c:set var="delete" value="/delete"/>
<c:set var="logout" value="/logout"/>
<c:set var="create" value="/create"/>
<c:set var="login" value="/login"/>

<!-- Objects -->
<jsp:useBean id="users" scope="request" type="java.util.List"/>
<jsp:useBean id="dateTime" scope="request" class="java.util.Date"/>
<%--@elvariable id="usersInfo" type="java.util.List"--%>


<html>
<head>
    <title>User list</title>
    <c:import url="imports/i_headLibraries.jsp"/>
</head>
<body>

<!-- Navigation bar -->
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <form class="navbar-form navbar-left" action="${context}" method="GET">
            <button type="submit" class="btn btn-primary navbar-btn">Home</button>
        </form>
        <form class="navbar-form navbar-left" action="${context}${create}" method="GET">
            <button type="submit" class="btn btn-primary navbar-btn">Create user</button>
        </form>
        <%--@elvariable id="loggedUser" type="ru.job4j.crud.model.User"--%>
        <%--@elvariable id="loggedUserInfo" type="java.util.List"--%>
        <c:if test="${loggedUser != null}">
            <div class="nav navbar-nav navbar-right">
                <div class="row">
                    <div class="col-sm-8">
                        <p class="navbar-text">Logged: ${loggedUserInfo[0]} (id: ${loggedUser.id},
                            role: ${loggedUser.credentials.role})</p>
                    </div>
                    <div class="col-sm-4">
                        <form class="navbar-form" action="${context}${logout}" method="POST">
                            <button type="submit" class="btn btn-primary navbar-btn">Logout</button>
                        </form>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</nav>

<!-- Error messages show -->
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

<!-- User list table -->
<div class="container col-sm-offset-2 col-sm-8">
    <h2>User list</h2>
    <p>Users registered in the system</p>
    <table class="table table-hover">
        <thead>
        <tr>
            <th>Id</th>
            <th>Created</th>
            <th>Login</th>
            <th>Role</th>
            <th>Name</th>
            <th>Email</th>
            <th>Country</th>
            <th>City</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${users}" var="user" varStatus="ind">
            <tr>
                <td><c:out value="${user.id}"/>
                </td>
                <td>
                    <jsp:setProperty name="dateTime" property="time" value="${user.created}"/>
                    <fmt:formatDate value="${dateTime}" pattern="dd.MM.yyyy HH:mm:ss"/>
                </td>
                <td><c:out value="${user.credentials.login}"/>
                </td>
                <td><c:out value="${user.credentials.role}"/>
                </td>
                <td>
                    <c:out value="${usersInfo[ind.index][0]}"/>
                </td>
                <td><c:out value="${usersInfo[ind.index][1]}"/>
                </td>
                <td><c:out value="${usersInfo[ind.index][2]}"/>
                </td>
                <td><c:out value="${usersInfo[ind.index][3]}"/>
                </td>
                <td>
                    <div class="row">
                        <div class="col-sm-6">
                            <form action="${context}${update}" method="get">
                                <input type="hidden" name="id" value="${user.id}">
                                <button type="submit" class="btn btn-default">Update</button>
                            </form>
                        </div>
                        <div class="col-sm-6">
                            <form action="${context}${delete}" method="get">
                                <input type="hidden" name="id" value="${user.id}">
                                <button type="submit" class="btn btn-default">Delete</button>
                            </form>
                        </div>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

</body>
</html>
