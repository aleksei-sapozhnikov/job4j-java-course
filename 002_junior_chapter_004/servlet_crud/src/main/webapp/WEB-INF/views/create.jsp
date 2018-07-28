<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="context" scope="request" value="${pageContext.request.contextPath}"/>
<%--@elvariable id="error" type="java.lang.String"--%>

<html>
<head>
    <!-- Title and libraries -->
    <jsp:include page="imports/head.jsp">
        <jsp:param name="title" value="Create user"/>
    </jsp:include>
    <!-- Scripts -->
    <jsp:include page="scripts/findInvalids.jsp"/>
    <jsp:include page="scripts/submitUserFormIfFieldsValid.jsp"/>
</head>

<body>

<!-- Navigation bar -->
<jsp:include page="imports/navbar.jsp"/>

<!-- Error messages show -->
<jsp:include page="imports/errorShow.jsp">
    <jsp:param name="error" value="${error}"/>
</jsp:include>

<!-- User creation form -->
<div class="container col-sm-offset-2 col-sm-8">
    <h2>Create user</h2>
    <p>Fill the form and click "Submit" button to create a new user</p>
    <form id="user_form" class="form-horizontal"
          action="${context}${initParam.create}" method="POST">
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
        <!-- Role -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="role">Role:</label>
            <div class="col-sm-10">
                <select name="role" class="form-control" id="role">
                    <%--@elvariable id="roles" type="java.util.Collection"--%>
                    <c:forEach items="${roles}" var="role">
                        <option value="${role}">
                                ${role}
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <!-- Name -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="city">Name:</label>
            <div class="col-sm-10">
                <input name="name" type="text" class="form-control" id="name"
                       placeholder="Enter name (e.g. John Sullivan)">
            </div>
        </div>
        <!-- Email -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="email">Email:</label>
            <div class="col-sm-10">
                <input name="email" type="email" class="form-control" id="email"
                       placeholder="Enter email (e.g. sullivan@netmail.com)">
            </div>
        </div>
        <!-- Country -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="country">Country:</label>
            <div class="col-sm-10">
                <input name="country" type="text" class="form-control" id="country"
                       placeholder="Enter country (e.g. Russia)">
            </div>
        </div>
        <!-- City -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="city">Name:</label>
            <div class="col-sm-10">
                <input name="city" type="text" class="form-control" id="city"
                       placeholder="Enter city (e.g. Moscow)">
            </div>
        </div>
        <!-- Submit button -->
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default"
                    onclick="return submitUserFormIfFieldsValid()">
                Submit
            </button>
        </div>
    </form>
</div>

</body>
</html>
