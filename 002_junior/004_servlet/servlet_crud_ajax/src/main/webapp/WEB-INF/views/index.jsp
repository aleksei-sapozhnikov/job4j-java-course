<%--@elvariable id="roles" type="java.util.List<Role>"--%>
<%--@elvariable id="loggedUser" type="ru.job4j.crud.model.User"--%>
<%--@elvariable id="users" type="java.util.List<ru.job4j.crud.model.User>"--%>
<%--@elvariable id="error" type="java.lang.String"--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="dateTime" scope="request" class="java.util.Date"/>
<c:set var="context" scope="request" value="${pageContext.request.contextPath}"/>

<!doctype html>
<html lang="en">
<head>
    <title>User list</title>

    <!-- Head common attrs -->
    <c:import url="head/head-common-attrs.jsp"/>

    <!-- Custom Scripts -->
    <c:import url="scripts/formatDateTime.jsp"/>
    <c:import url="scripts/pageActionsListeners.jsp"/>
    <c:import url="scripts/userCreate.jsp"/>
    <c:import url="scripts/userUpdate.jsp"/>
    <c:import url="scripts/userDelete.jsp"/>
    <c:import url="scripts/input.jsp"/>
    <c:import url="scripts/userValidation.jsp"/>
    <c:import url="scripts/selectorsFill.jsp"/>

</head>
<body>

<!-- Navigation bar -->
<c:import url="page-parts/navbar.jsp"/>

<!-- User create dialog (for modal window) -->
<c:import url="page-parts/user-create-dialog.jsp"/>

<!-- User update dialog (for modal window) -->
<c:import url="page-parts/user-update-dialog.jsp"/>

<!-- Show all users table -->
<c:import url="page-parts/show-all-users-table.jsp"/>

</body>
</html>