<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="context" scope="request" value="${pageContext.request.contextPath}"/>
<%--@elvariable id="error" type="java.lang.String"--%>

<html>
<head>
    <jsp:include page="imports/head.jsp">
        <jsp:param name="title" value="Login"/>
    </jsp:include>
</head>
<body>

<!-- Navigation bar -->
<jsp:include page="imports/navigationBar.jsp"/>

<!-- Error messages show -->
<jsp:include page="imports/errorShow.jsp">
    <jsp:param name="error" value="${error}"/>
</jsp:include>

<!-- Login form -->
<div class="container col-sm-offset-2 col-sm-8">
    <h2>Login</h2>
    <p>Enter user login and password</p>
    <form class="form-horizontal" action="${context}${initParam.login}" method="POST">
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
