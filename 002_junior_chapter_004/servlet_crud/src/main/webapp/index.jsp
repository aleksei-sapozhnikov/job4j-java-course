<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Заголовок</title>
</head>
<body>

<a href="collection.jsp">Коллекция</a>
<br><br>
<a href="database.jsp">База данных</a>
<br><br>
<%=request.getContextPath()%>
<br><br>
<%=request.getServletPath()%>

</body>
</html>
