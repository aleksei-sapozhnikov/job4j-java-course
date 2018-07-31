<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="context" scope="request" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <jsp:include page="imports/head.jsp">
        <jsp:param name="title" value="User List"/>
    </jsp:include>

    <script>
        $(document).ready(function () {
            $('#action').on('change', function () {
                var value = $("[name=action]:checked").val();
                alert(value);
            })
        });
    </script>


</head>

<style>
    @media (min-width: 768px) {
        .sidebar {
            position: fixed;
        }
    }
</style>

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
                <label class="control-label col-sm-2">Sex:</label>
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


            <!-- Create user -->
            <c:import url="imports/create_form.jsp"/>
        </div>
    </div>
</div>

</body>
</html>