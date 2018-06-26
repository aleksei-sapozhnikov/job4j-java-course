<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Заголовок</title>
</head>
<body>

<form action="<%=request.getContextPath()%>/collection">
    <p align="center"><input type="submit" value="Collection"></p>
</form>
<form action="<%=request.getContextPath()%>/database">
    <p align="center"><input type="submit" value="Database"></p>
</form>
</body>
</html>
