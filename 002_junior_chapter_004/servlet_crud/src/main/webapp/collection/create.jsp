<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create user</title>
</head>
<body>

<div align="center">
    <h1>Create user</h1>
</div>

<form action="<%=request.getContextPath()%>/collection/create" method="post">
    <table style="border: 0" align="center">
        <tr>
            <td align="left">User name:</td>
            <td><input type="text" name="name"></td>
        </tr>
        <tr>
            <td align="left">User login:</td>
            <td><input type="text" name="login"></td>
        </tr>
        <tr>
            <td align="left">User email:</td>
            <td><input type="text" name="email"></td>
        </tr>
    </table>
    <p align="center">
        <input type="submit" value="create">
    </p>
</form>
</body>
</html>
