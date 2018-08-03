<%--@elvariable id="roles" type="java.util.List<Role>"--%>
<%--@elvariable id="loggedUser" type="ru.job4j.crud.model.User"--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="dateTime" scope="request" class="java.util.Date"/>
<c:set var="context" scope="request" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>
        User list
    </title>

    <!-- Bootstrap and jquery -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>

<style>
    @media (min-width: 768px) {
        .sidebar {
            position: fixed;
        }
    }
</style>

<body>

<!-- Navigation bar -->
<c:import url="imports/navigationBar.jsp"/>

<!-- Main container -->
<div class="container-fluid">
    <!-- User list -->
    <div class="row">
        <div class="col-sm-8">
            <h3> Users list: </h3>
            <c:import url="imports/tableOfUsers.jsp"/>
        </div>
    </div>
</div>

</body>
</html>