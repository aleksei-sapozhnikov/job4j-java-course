<%--@elvariable id="roles" type="java.util.List<Role>"--%>
<%--@elvariable id="loggedUser" type="ru.job4j.crud.model.User"--%>
<%--@elvariable id="users" type="java.util.List<ru.job4j.crud.model.User>"--%>
<%--@elvariable id="error" type="java.lang.String"--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="dateTime" scope="request" class="java.util.Date"/>
<c:set var="context" scope="request" value="${pageContext.request.contextPath}"/>

<div id="users-contain" class="container-fluid">
    <h1>Existing Users:</h1>
    <table id="users" class="table table-hover">
        <thead>
        <tr>
            <th>Identity</th>
            <th>Credentials</th>
            <th>Info</th>
            <th align="center">Actions:</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${users}" var="user">
            <tr id="users-row-${user.id}">
                <td id="users-row-${user.id}-id-created">
                    <div><b>Id: </b>${user.id}</div>
                    <div>
                        <b>Created: </b>
                        <jsp:setProperty name="dateTime" property="time" value="${user.created}"/>
                        <fmt:formatDate value="${dateTime}" pattern="dd.MM.yyyy HH:mm:ss"/>
                    </div>
                </td>
                <td id="users-row-${user.id}-credentials">
                    <div><b>Login: </b>${user.credentials.login}</div>
                    <div><b>Role: </b>${user.credentials.role}</div>
                </td>
                <td id="users-row-${user.id}-info">
                    <div><b>Name: </b>${user.info.name}</div>
                    <div><b>Email: </b>${user.info.email}</div>
                    <div><b>Country: </b>${user.info.country}</div>
                    <div><b>City: </b>${user.info.city}</div>
                </td>
                <td>
                    <div class="row">
                        <div class="col-sm-6">
                            <form action="${context}${initParam.update}" method="post">
                                <input name="id" type="hidden" value="${user.id}"/>
                                <input type="button" class="btn btn-primary update-specific-user-button"
                                       data-toggle="modal" data-target="#user-update-dialog" value="Update"
                                       onclick="updateUserFormFill(${user.id})"/>
                            </form>
                        </div>
                        <div class="col-sm-6">
                            <form action="${context}${initParam.update}" method="post">
                                <input name="id" type="hidden" value="${user.id}"/>
                                <input id="delete_${user.id}" type="button" class="btn btn-primary" value="Delete"
                                       onclick="beginDeleteUser(${user.id})"/>
                            </form>
                        </div>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
