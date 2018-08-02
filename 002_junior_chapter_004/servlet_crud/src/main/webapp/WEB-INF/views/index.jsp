<%--@elvariable id="roles" type="java.util.List<Role>"--%>
<%--@elvariable id="loggedUser" type="ru.job4j.crud.model.User"--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="context" scope="request" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <jsp:include page="imports/head.jsp">
        <jsp:param name="title" value="User List"/>
    </jsp:include>
</head>

<style>
    @media (min-width: 768px) {
        .sidebar {
            position: fixed;
        }
    }
</style>

<c:import url="scripts/findInvalids.jsp"/>
<c:import url="scripts/submitUserFormIfFieldsValid.jsp"/>

<script>
    String.prototype.format = function () {
        let formatted = this;
        for (let i = 0; i < arguments.length; i++) {
            const regexp = new RegExp('\\{' + i + '\\}', 'gi');
            formatted = formatted.replace(regexp, arguments[i]);
        }
        return formatted;
    };
</script>

<script>
    $(document).ready(function () {
        $('#action').on('change', function () {
            var action = $("[name=action]:checked").val();
            var form;
            switch (action) {
                case 'create':
                    form = 'create_form';
                    break;
                case 'update':
                    form = 'update_form';
                    fillValues($('#update_id').val());
                    break;
                case 'delete':
                    form = 'delete_form';
                    break;
            }
            changeForm(form);
        })
    });
</script>

<script>
    function changeForm(form) {
        document.getElementById('action_form').innerHTML =
            document.getElementById(form).innerHTML
    }
</script>

<body>

<c:import url="imports/navbar.jsp"/>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-8">
            <!-- User list -->
            <c:import url="imports/user_list.jsp"/>
        </div>
        <div class="col-sm-offset-8 col-sm-4 sidebar">
            <!-- Action switcher -->
            <div class="form-group" id="action">
                <label class="control-label col-sm-2">Action:</label>
                <div class="col-sm-10">
                    <label class="radio-inline">
                        <input type="radio" name="action" value="create"/>Create
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="action" value="update"/>Update
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="action" value="delete"/>Delete
                    </label>
                </div>
            </div>
            <!-- Action form -->
            <div id="action_form"></div>
        </div>
    </div>
</div>

<!-- Create user form -->
<div id="create_form" style="display:none">
    <c:import url="forms/create_form.jsp"/>
</div>
<!-- Update user form -->
<div id="update_form" style="display:none">
    <c:import url="forms/update_form.jsp"/>
</div>
<!-- Delete user form -->
<div id="delete_form" style="display:none">
    <c:import url="forms/delete_form.jsp"/>
</div>

</body>
</html>