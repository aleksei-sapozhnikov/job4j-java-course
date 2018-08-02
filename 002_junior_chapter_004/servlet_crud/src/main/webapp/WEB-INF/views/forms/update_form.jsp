<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="context" scope="request" value="${pageContext.request.contextPath}"/>
<%--@elvariable id="roles" type="java.util.Collection"--%>
<%--@elvariable id="users" type="java.util.List<ru.job4j.crud.model.User>"--%>

<script>
    function changeValues() {
        fillValues(getId());
        return false;
    }

    function getId() {
        return document.getElementById('update_id').value;
    }

    function fillValues(id) {
        var result = 'none';
        $.ajax({
            type: "POST",
            url: location.href + "/ajax",
            data: id,
            success: function (user) {
                $('#update_login').val(user.credentials.login);
                $('#update_password').val(user.credentials.password);
                $('#update_role').val(user.credentials.role);
                $('#update_name').val(user.info.name);
                $('#update_email').val(user.info.email);
                $('#update_country').val(user.info.country);
                $('#update_city').val(user.info.city);
            }
        });
        return result;
    }
</script>

<div class="container-fluid">
    <h2>Update user</h2>
    <form class="form-horizontal" id="user_form"
          action="${context}${initParam.update}" method="POST">
        <!-- Id of user to delete -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="update_id">Id:</label>
            <div class="col-sm-10">
                <select name="id" class="form-control" id="update_id" onchange="return changeValues()">
                    <c:forEach items="${users}" var="user">
                        <option value="${user.id}">
                                ${user.id}
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <!-- Login -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="update_login">Login:</label>
            <div class="col-sm-10">
                <input name="login" type="text" class="form-control" id="update_login"
                       placeholder="Enter login (e.g. nagibator2000)">
            </div>
        </div>
        <!-- Password -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="update_password">Password:</label>
            <div class="col-sm-10">
                <input name="password" type="password" class="form-control" id="update_password"
                       placeholder="Enter password (e.g. qwerty123)">
            </div>
        </div>
        <!-- Role -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="update_role">Role:</label>
            <div class="col-sm-10">
                <select name="role" class="form-control" id="update_role">
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
            <label class="control-label col-sm-2" for="update_name">Name:</label>
            <div class="col-sm-10">
                <input name="name" type="text" class="form-control" id="update_name"
                       placeholder="Enter name (e.g. John Sullivan)">
            </div>
        </div>
        <!-- Email -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="update_email">Email:</label>
            <div class="col-sm-10">
                <input name="email" type="email" class="form-control" id="update_email"
                       placeholder="Enter email (e.g. sullivan@netmail.com)">
            </div>
        </div>
        <!-- Country -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="update_country">Country:</label>
            <div class="col-sm-10">
                <input name="country" type="text" class="form-control" id="update_country"
                       placeholder="Enter country (e.g. Russia)">
            </div>
        </div>
        <!-- City -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="update_city">City:</label>
            <div class="col-sm-10">
                <input name="city" type="text" class="form-control" id="update_city"
                       placeholder="Enter city (e.g. Moscow)">
            </div>
        </div>
        <!-- Submit button -->
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default"
                    onclick="submitUserFormIfFieldsValid()">
                Submit
            </button>
        </div>
    </form>
</div>