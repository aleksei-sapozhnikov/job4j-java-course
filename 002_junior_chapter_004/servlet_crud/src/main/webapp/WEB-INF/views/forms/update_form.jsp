<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <!-- User input fields -->
        <C:import url="user_inputs.jsp"/>
        <!-- Submit button -->
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default"
                    onclick="validateAndSubmit()">
                Submit
            </button>
        </div>
    </form>
</div>