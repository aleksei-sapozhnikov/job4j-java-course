<%@ page import="ru.job4j.crud.User" %>
<%@ page import="ru.job4j.crud.collection.UserValidatorInCollection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update user</title>
</head>
<body>

<div align="center">
    <h1>Update user</h1>
</div>

<form action="<%=request.getContextPath()%>/collection/update" method="post">
    <% User user = UserValidatorInCollection.getInstance().findById(
            Integer.valueOf(request.getParameter("id"))
    );%>
    <table style="border: 0" align="center">
        <tr>
            <td align="left">User name:</td>
            <td><input type="text" name="name" value="<%=user.getName()%>"></td>
        </tr>
        <tr>
            <td align="left">User login:</td>
            <td><input type="text" name="login" value="<%=user.getLogin()%>"></td>
        </tr>
        <tr>
            <td align="left">User email:</td>
            <td><input type="text" name="email" value="<%=user.getEmail()%>"></td>
        </tr>
    </table>
    <p align="center">
        <input type="hidden" name="id" value="<%=user.getId()%>">
        <input type="submit" value="update">
    </p>
</form>

</body>
</html>
