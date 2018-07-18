<%--@elvariable id="roles" type="java.util.List<Role>"--%>
<%--@elvariable id="loggedUser" type="ru.job4j.crud.model.User"--%>
<%--@elvariable id="users" type="java.util.List<ru.job4j.crud.model.User>"--%>
<%--@elvariable id="error" type="java.lang.String"--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="dateTime" scope="request" class="java.util.Date"/>
<c:set var="context" scope="request" value="${pageContext.request.contextPath}"/>

<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container-fluid">
        <form class="navbar-form navbar-left" action="${context}" method="GET">
            <button class="btn btn-primary navbar-btn">Home</button>
        </form>
        <c:if test="${loggedUser != null}">
            <form class="navbar-form navbar-left">
                <button id="user-create-button" class="btn btn-primary navbar-btn"
                        data-toggle="modal" data-target="#user-create-dialog">Create user
                </button>
            </form>
            <form class="navbar-form navbar-left">
                <button id="user-update-button" class="btn btn-primary navbar-btn"
                        data-toggle="modal" data-target="#user-update-dialog">Update user
                </button>
            </form>
            <div class="nav navbar-nav navbar-right">
                <div class="row">
                    <div class="col-sm-8">
                        <p class="navbar-text">Logged:&nbsp${loggedUser.info.name},
                            id:&nbsp${loggedUser.id}, role:&nbsp${loggedUser.credentials.role}</p>

                    </div>
                    <div class="col-sm-4">
                        <form class="navbar-form" action="${context}${initParam.logout}" method="POST">
                            <button type="submit" class="btn btn-primary navbar-btn">Logout</button>
                        </form>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</nav>