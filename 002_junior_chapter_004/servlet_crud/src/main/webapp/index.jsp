<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Заголовок</title>
</head>
<body>

<div align="center">
    <h1>User store user</h1>
</div>

<form action="<%=request.getContextPath()%>/collection">
    <div align="center"><input type="submit" value="Collection"></div>
</form>
<form action="<%=request.getContextPath()%>/database">
    <div align="center"><input type="submit" value="Database"></div>
</form>
</body>
</html>
