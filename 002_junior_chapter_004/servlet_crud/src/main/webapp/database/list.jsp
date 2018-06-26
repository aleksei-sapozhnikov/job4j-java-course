<%@ page import="ru.job4j.crud.User" %>
<%@ page import="ru.job4j.crud.database.UserValidatorInDatabase" %>
<%@ page import="java.time.Instant" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.ZoneId" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User list</title>
</head>
<body>

<p align="center">
    <a href="../index.jsp">Index page</a>
</p>
<form action="<%=request.getContextPath()%>/database/create" method="get">
    <p align="center"><input type="submit" value="create user"></p>
</form>

<div align="center">
    <h1>Users list: database</h1>
</div>

<table style="border: 1px solid black;" cellpadding="5px" cellspacing="0px" border="1px" align="center" valign="center">
    <tr align="center" valign="center">
        <th>id</th>
        <th>name</th>
        <th>login</th>
        <th>email</th>
        <th>created</th>
        <th colspan="2">Actions</th>
    </tr>
    <% for (User user : UserValidatorInDatabase.getInstance().findAll()) { %>
    <tr align="center" valign="center">
        <td><%=user.getId()%>
        </td>
        <td><%=user.getName()%>
        </td>
        <td><%=user.getLogin()%>
        </td>
        <td><%=user.getEmail()%>
        </td>
        <td><%=LocalDateTime.ofInstant(Instant.ofEpochMilli(
                user.getCreated()
        ), ZoneId.systemDefault()).toString()%>
        </td>
        <td>
            <form action="<%=request.getContextPath()%>/database/update" method="get">
                <input type="hidden" name="id" value="<%=Integer.toString(user.getId())%>">
                <input type="submit" value="update">
            </form>
        </td>
        <td>
            <form action="<%=request.getContextPath()%>/database" method="post">
                <input type="hidden" name="id" value="<%=Integer.toString(user.getId())%>">
                <input type="submit" value="delete">
            </form>
        </td>
    </tr>
    <% } %>
</table>
</body>
</html>
