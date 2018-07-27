<%--@elvariable id="context" type="java.lang.String"--%>
<%--@elvariable id="create" type="java.lang.String"--%>
<%--@elvariable id="update" type="java.lang.String"--%>
<%--@elvariable id="delete" type="java.lang.String"--%>
<%--@elvariable id="logout" type="java.lang.String"--%>
<%--@elvariable id="error" type="java.lang.String"--%>
<%--@elvariable id="roles" type="java.util.Collection"--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="loggedUser" scope="session" type="ru.job4j.crud.model.User"/>
<jsp:useBean id="user" scope="request" type="ru.job4j.crud.model.User"/>

<html>
<head>
    <jsp:include page="imports/head.jsp">
        <jsp:param name="title" value="Update user"/>
    </jsp:include>
</head>
<body>

<!-- Navigation bar -->
<jsp:include page="imports/navbar.jsp"/>

<!-- Error messages show -->
<jsp:include page="imports/errorShow.jsp">
    <jsp:param name="error" value="${error}"/>
</jsp:include>

<!-- User update form -->
<div class="container col-sm-offset-2 col-sm-8">
    <h2>Update user</h2>
    <p>Fill the form and click "Submit" button to update user values</p>
    <form class="form-horizontal" action="${context}${update}" method="POST">
        <!-- Login -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="login">Login:</label>
            <div class="col-sm-10">
                <input name="login" type="text" class="form-control" id="login" value="${user.credentials.login}"
                       placeholder="Enter login (e.g. nagibator2000)">
            </div>
        </div>
        <!-- Password -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="login">Password:</label>
            <div class="col-sm-10">
                <input name="password" type="password" class="form-control" id="password"
                       value="${user.credentials.password}"
                       placeholder="Enter password (e.g. qwerty123)">
            </div>
        </div>
        <!-- Role -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="role">Role:</label>
            <div class="col-sm-10">
                <select name="role" class="form-control" id="role">
                    <c:forEach items="${roles}" var="role">
                        <c:choose>
                            <c:when test="${role == user.credentials.role}">
                                <option value="${role}" selected="selected">${role}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${role}">${role}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>
        </div>
        <!-- Name -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="name">Name:</label>
            <div class="col-sm-10">
                <input name="name" type="text" class="form-control" id="name" value="${user.info.name}"
                       placeholder="Enter name (e.g. John Sullivan)">
            </div>
        </div>
        <!-- Email -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="email">Email:</label>
            <div class="col-sm-10">
                <input name="email" type="email" class="form-control" id="email" value="${user.info.email}"
                       placeholder="Enter email (e.g. sullivan@netmail.com)">
            </div>
        </div>
        <!-- Country -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="country">Country:</label>
            <div class="col-sm-10">
                <input name="country" type="text" class="form-control" id="country" value="${user.info.country}"
                       placeholder="Enter country (e.g. Russia)">
            </div>
        </div>
        <!-- City -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="city">City:</label>
            <div class="col-sm-10">
                <input name="city" type="text" class="form-control" id="city" value="${user.info.city}"
                       placeholder="Enter city (e.g. Moscow)">
            </div>
        </div>
        <!-- Logged user id: to check permissions -->
        <input type="hidden" name="loggedUserId" value="${loggedUser.id}">
        <!-- Submit button -->
        <div class="col-sm-offset-2 col-sm-10">
            <input type="hidden" name="id" value="${user.id}">
            <button type="submit" class="btn btn-default">Submit</button>
        </div>
    </form>
</div>

</body>
</html>
