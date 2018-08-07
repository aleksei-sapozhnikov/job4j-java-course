<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="context" scope="request" value="${pageContext.request.contextPath}"/>
<jsp:useBean id="dateTime" scope="request" class="java.util.Date"/>
<%--@elvariable id="users" type="java.util.List<ru.job4j.crud.model.User>"--%>
<%--@elvariable id="error" type="java.lang.String"--%>

<html>
<head>
    <jsp:include page="../imports/head.jsp">
        <jsp:param name="title" value="User List"/>
    </jsp:include>

    <!-- Scripts -->
    <jsp:include page="../scripts-old/findInvalids.jsp"/>
    <jsp:include page="../scripts-old/submitUserFormIfFieldsValid.jsp"/>
</head>

<script>
    $(function () {
        var availableTags = [
            "ActionScript", "AppleScript", "Asp", "BASIC", "C", "C++",
            "Clojure", "COBOL", "ColdFusion", "Erlang", "Fortran",
            "Groovy", "Haskell", "Java", "JavaScript", "Lisp", "Perl",
            "PHP", "Python", "Ruby", "Scala", "Scheme"
        ];

        $(".autocomplete").autocomplete({
            source: availableTags
        });
    });
</script>


<div class="container">

    <div class="form-group">
        <label>Languages</label>
        <input class="form-control autocomplete" placeholder="Enter A" />
    </div>

    <div class="form-group">
        <label >Another Field</label>
        <input class="form-control">
    </div>

</div>

<!-- Navigation bar -->
<jsp:include page="../imports/navigationBar.jsp"/>

<!-- Error messages show -->
<jsp:include page="../imports/errorShow.jsp">
    <jsp:param name="error" value="${error}"/>
</jsp:include>

<!-- User fill form -->
<div class="container col-sm-offset-2 col-sm-8">
    <jsp:include page="userForm.jsp"/>
</div>

<!-- List of users -->
<div class="container col-sm-offset-1 col-sm-10">
    <jsp:include page="list.jsp"/>
</div>

</html>

<body>