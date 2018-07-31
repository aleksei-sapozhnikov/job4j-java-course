<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="context" scope="request" value="${pageContext.request.contextPath}"/>
<jsp:useBean id="dateTime" scope="request" class="java.util.Date"/>
<%--@elvariable id="users" type="java.util.List<ru.job4j.crud.model.User>"--%>

<!-- User list table -->
<h2>User list</h2>
<p>Users registered in the system</p>
<table class="table table-hover">
    <thead>
    <tr>
        <th>Identity</th>
        <th>Credentials</th>
        <th>Info</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${users}" var="user" varStatus="ind">
        <tr>
            <td>
                <div><b>Id: </b>${user.id}</div>
                <div>
                    <b>Created: </b>
                    <jsp:setProperty name="dateTime" property="time" value="${user.created}"/>
                    <fmt:formatDate value="${dateTime}" pattern="dd.MM.yyyy HH:mm:ss"/>
                </div>
            </td>
            <td>
                <div><b>Login: </b>${user.credentials.login}</div>
                <div><b>Role: </b>${user.credentials.role}</div>
            </td>
            <td>
                <div><b>Name: </b>${user.info.name}</div>
                <div><b>Email: </b>${user.info.email}</div>
                <div><b>Country: </b>${user.info.country}</div>
                <div><b>City: </b>${user.info.city}</div>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>