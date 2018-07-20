<%@ page contentType="text/html;charset=UTF-8" %>

<!-- JSP libraries -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Paths -->
<c:set var="context" value="${pageContext.request.contextPath}"/>
<c:set var="create" value="/create"/>
<c:set var="update" value="/update"/>
<c:set var="delete" value="/delete"/>
<c:set var="logout" value="/logout"/>
<c:set var="create" value="/create"/>
<c:set var="login" value="/login"/>

<html>
<head>

    <title>Create user</title>
    <c:import url="imports/i_headLibraries.jsp"/>

    <script>
        /**
         * Returns all invalid fields.
         * @returns {object} true if valid, false if not.
         */
        function findInvalids(name, login, password, email) {
            var invalids = [];
            // check name
            if (name === "") {
                invalids.push("name");
            }
            // check login
            if (login === "") {
                invalids.push("login");
            }
            // check password
            if (password === "") {
                invalids.push("password");
            }
            // check email
            if (email === "" || !email.includes("@")) {
                invalids.push("email");
            }
            return invalids;
        }

        /**
         * Adds row to users table if user values are valid.
         * @returns {boolean} true if user was added, false if not.
         */
        function createUserIfFieldsValid() {
            var result;
            var name = $('#name').val();
            var login = $('#login').val();
            var password = $("#password").val();
            var email = $('#email').val();
            var invalids = findInvalids(name, login, password, email);
            if (invalids.length === 0) {
                $('#create_user_form').submit();
                result = true;
            } else {
                alert("Values are invalid: " + invalids);
                result = false;
            }
            return result;
        }
    </script>
</head>

<body>

<!-- Navigation bar -->
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <form class="navbar-form navbar-left" action="${context}" method="GET">
            <button type="submit" class="btn btn-primary navbar-btn">Home</button>
        </form>
        <form class="navbar-form navbar-left" action="${context}${create}" method="GET">
            <button type="submit" class="btn btn-primary navbar-btn">Create user</button>
        </form>
        <%--@elvariable id="loggedUser" type="ru.job4j.crud.User"--%>
        <c:if test="${loggedUser != null}">
            <div class="nav navbar-nav navbar-right">
                <div class="row">
                    <div class="col-sm-8">
                        <p class="navbar-text">Logged: ${loggedUser.name} (id: ${loggedUser.id},
                            role: ${loggedUser.role})</p>
                    </div>
                    <div class="col-sm-4">
                        <form class="navbar-form" action="${context}${logout}" method="POST">
                            <button type="submit" class="btn btn-primary navbar-btn">Logout</button>
                        </form>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</nav>

<!-- Error messages show -->
<%--@elvariable id="error" type="java.lang.String"--%>
<c:if test="${error != null}">
    <div class="text-center">
        <div class="alert alert-danger">
            <strong>Error!</strong> ${error}
        </div>
    </div>
</c:if>
<c:if test="${param.error != null}">
    <div class="text-center">
        <div class="alert alert-danger">
            <strong>Error!</strong> ${param.error}
        </div>
    </div>
</c:if>

<!-- User creation form -->
<div class="container col-sm-offset-2 col-sm-8">
    <h2>Create user</h2>
    <p>Fill the form and click "Submit" button to create a new user</p>
    <form id="create_user_form" class="form-horizontal"
          action="${context}${create}" method="POST">
        <!-- Name -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="name">Name:</label>
            <div class="col-sm-10">
                <input name="name" type="text" class="form-control" id="name"
                       placeholder="Enter name (e.g. John Sullivan)">
            </div>
        </div>
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
        <!-- Email -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="email">Email:</label>
            <div class="col-sm-10">
                <input name="email" type="email" class="form-control" id="email"
                       placeholder="Enter email (e.g. sullivan@netmail.com)">
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
        <!-- Submit button -->
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default"
                    onclick="return createUserIfFieldsValid()">
                Submit
            </button>
        </div>
    </form>
</div>

</body>
</html>
