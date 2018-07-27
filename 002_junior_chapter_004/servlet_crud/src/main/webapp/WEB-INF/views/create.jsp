<%--@elvariable id="context" type="java.lang.String"--%>
<%--@elvariable id="create" type="java.lang.String"--%>
<%--@elvariable id="update" type="java.lang.String"--%>
<%--@elvariable id="delete" type="java.lang.String"--%>
<%--@elvariable id="logout" type="java.lang.String"--%>
<%--@elvariable id="error" type="java.lang.String"--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="loggedUser" scope="session" type="ru.job4j.crud.model.User"/>

<html>
<head>
    <jsp:include page="imports/head.jsp">
        <jsp:param name="title" value="Create user"/>
    </jsp:include>

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
<jsp:include page="imports/navbar.jsp"/>

<!-- Error messages show -->
<jsp:include page="imports/errorShow.jsp">
    <jsp:param name="error" value="${error}"/>
</jsp:include>

<!-- User creation form -->
<div class="container col-sm-offset-2 col-sm-8">
    <h2>Create user</h2>
    <p>Fill the form and click "Submit" button to create a new user</p>
    <form id="create_user_form" class="form-horizontal"
          action="${context}${create}" method="POST">
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
        <!-- Name -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="city">Name:</label>
            <div class="col-sm-10">
                <input name="name" type="text" class="form-control" id="name"
                       placeholder="Enter name (e.g. John Sullivan)">
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
        <!-- Country -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="country">Country:</label>
            <div class="col-sm-10">
                <input name="country" type="text" class="form-control" id="country"
                       placeholder="Enter country (e.g. Russia)">
            </div>
        </div>
        <!-- City -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="city">Name:</label>
            <div class="col-sm-10">
                <input name="city" type="text" class="form-control" id="city"
                       placeholder="Enter city (e.g. Moscow)">
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
