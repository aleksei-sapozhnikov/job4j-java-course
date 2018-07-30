<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="context" scope="request" value="${pageContext.request.contextPath}"/>
<%--@elvariable id="error" type="java.lang.String"--%>
<%--@elvariable id="countries" type="java.util.Collection"--%>
<%--@elvariable id="roles" type="java.util.Collection"--%>

<html>
<head>
    <!-- Title and libraries -->
    <jsp:include page="imports/head.jsp">
        <jsp:param name="title" value="Create user"/>
    </jsp:include>
    <!-- Scripts -->

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            requestList('country_ajax_list', 'countries', 'country');
            requestList('city_ajax_list', 'cities', 'city');
        }, false);
    </script>

    <script>
        function requestList(divId, listName, sendName) {
            $.ajax({
                type: "POST",
                url: "${context}" + "${initParam.ajax}",
                data: listName,
                success: function (resp) {
                    $('#' + divId).html(makeHtmlSelectFromList(resp, sendName));
                }
            });
            return false;
        }
    </script>

    <script>
        function makeHtmlSelectFromList(list, sendName) {
            var result = "<select name=\"" + sendName + "\" class=\"form-control\">";
            list.forEach(function (value) {
                    result +=
                        "<option value=\"" + value + "\">" +
                        value +
                        "</option>"
                }
            );
            result += "</select>";
            return result;
        }
    </script>

    <script>
        /**
         * Adds row to users table if user values are valid.
         * @returns {boolean} true if user was added, false if not.
         */
        function submitUserFormIfFieldsValid() {
            var result;
            var name = $('#name').val();
            var login = $('#login').val();
            var password = $("#password").val();
            var email = $('#email').val();
            // country
            var countryFromForm = $('#country_form').val();
            var country = countryFromForm === "" ? $('#country_ajax_list') : countryFromForm;
            // city
            var cityFromForm = $('#city_form').val();
            var city = cityFromForm === "" ? $('#city_ajax_list') : cityFromForm;
            var invalids = findInvalids(name, login, password, email, country, city);
            if (invalids.length === 0) {
                var form = $('#user_form').val();
                form.setAttribute("country", country);
                form.setAttribute("city", city);
                form.submit();
                result = true;
            } else {
                alert("Values are invalid: " + invalids);
                result = false;
            }
            return result;
        }
    </script>

    <script>
        /**
         * Returns all invalid fields.
         * @returns {object} true if valid, false if not.
         */
        function findInvalids(name, login, password, email, country, city) {
            var invalids = [];
            // check login
            if (login === "") {
                invalids.push("login");
            }
            // check password
            if (password === "") {
                invalids.push("password");
            }
            // check name
            if (name === "") {
                invalids.push("name");
            }
            // check email
            if (email === "" || !email.includes("@")) {
                invalids.push("email");
            }
            // check country
            if (country === "") {
                invalids.push("country");
            }
            // check city
            if (city === "") {
                invalids.push("city");
            }
            return invalids;
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
    <form id="user_form" class="form-horizontal"
          action="${context}${initParam.create}" method="POST">
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
            <div class="col-sm-10" id="country">
                <div class="col-sm" id="country_ajax_list">
                </div>
                <div class="col-sm">
                    <input type="text" class="form-control" id="country_form"
                           placeholder="Enter your own country if not found in list">
                </div>
            </div>
        </div>
        <!-- City -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="city">City:</label>
            <div class="col-sm-10" id="city">
                <div class="col-sm" id="city_ajax_list">
                </div>
                <div class="col-sm">
                    <input type="text" class="form-control" id="city_form"
                           placeholder="Enter your own city if not found in list">
                </div>
            </div>
        </div>
        <!-- Submit button -->
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default"
                    onclick="return submitUserFormIfFieldsValid()">
                Submit
            </button>
        </div>
    </form>
</div>

</body>
</html>
