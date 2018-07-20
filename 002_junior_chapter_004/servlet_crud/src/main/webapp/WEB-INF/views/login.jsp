<%@ page contentType="text/html;charset=UTF-8" %>

<!-- JSP libraries -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Paths -->
<c:set var="context" value="${pageContext.request.contextPath}"/>
<c:set var="create" value="/create"/>
<c:set var="update" value="/update"/>
<c:set var="delete" value="/delete"/>
<c:set var="logout" value="/logout"/>
<c:set var="create" value="/create"/>
<c:set var="login" value="/login"/>

<html>

<head>
    <title>Login</title>
    <c:import url="imports/i_headLibraries.jsp"/>
</head>

<body>

<!-- Paths -->
<c:set var="context" value="${pageContext.request.contextPath}"/>
<c:set var="create" value="/create"/>
<c:set var="update" value="/update"/>
<c:set var="delete" value="/delete"/>
<c:set var="logout" value="/logout"/>
<c:set var="create" value="/create"/>
<c:set var="login" value="/login"/>

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

<!-- Login form -->
<div class="container col-sm-offset-2 col-sm-8">
    <h2>Login</h2>
    <p>Enter user login and password</p>
    <form class="form-horizontal" action="${context}${login}" method="POST">
        <!-- Login -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="login">Login:</label>
            <div class="col-sm-10">
                <input name="login" type="text" class="form-control" id="login"
                       placeholder="Enter login (e.g. nagibator2000)">
            </div>
        </div>
        <!-- Password -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="login">Password:</label>
            <div class="col-sm-10">
                <input name="password" type="password" class="form-control" id="password"
                       placeholder="Enter password (e.g. qwerty123)">
            </div>
        </div>
        <!-- Submit button -->
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default">
                Submit
            </button>
        </div>
    </form>
</div>

</body>
</html>
