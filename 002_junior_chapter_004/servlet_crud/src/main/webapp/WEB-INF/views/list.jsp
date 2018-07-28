<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="context" scope="request" value="${pageContext.request.contextPath}"/>
<jsp:useBean id="dateTime" scope="request" class="java.util.Date"/>
<%--@elvariable id="users" type="java.util.List<ru.job4j.crud.model.User>"--%>
<%--@elvariable id="error" type="java.lang.String"--%>

<html>
<head>
    <jsp:include page="imports/head.jsp">
        <jsp:param name="title" value="User List"/>
    </jsp:include>
</head>
<body>

<!-- Navigation bar -->
<jsp:include page="imports/navbar.jsp"/>

<!-- Error messages show -->
<jsp:include page="imports/errorShow.jsp">
    <jsp:param name="error" value="${error}"/>
</jsp:include>

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
                <td><c:out value="${user.credentials.login}"/></td>
                <td><c:out value="${user.credentials.role}"/></td>
                <td><c:out value="${user.info.name}"/></td>
                <td><c:out value="${user.info.email}"/></td>
                <td><c:out value="${user.info.country}"/></td>
                <td><c:out value="${user.info.city}"/></td>
                <td>
                    <div class="row">
                        <div class="col-sm-6">
                            <form action="${context}${initParam.update}" method="get">
                                <input type="hidden" name="id" value="${user.id}">
                                <button type="submit" class="btn btn-default">Update</button>
                            </form>
                        </div>
                        <div class="col-sm-6">
                            <form action="${context}${initParam.delete}" method="get">
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
