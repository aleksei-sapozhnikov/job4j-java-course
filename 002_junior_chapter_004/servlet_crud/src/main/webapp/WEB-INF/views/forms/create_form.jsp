<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="context" scope="request" value="${pageContext.request.contextPath}"/>
<%--@elvariable id="roles" type="java.util.Collection"--%>

<div class="container-fluid">
    <h2>Create user</h2>
    <form class="form-horizontal" id="user_form"
          action="${context}${initParam.create}" method="POST">
        <!-- User input fields -->
        <c:import url="user_inputs.jsp"/>
        <!-- Submit button -->
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default"
                    onclick="return validateAndSubmit()">
                Submit
            </button>
        </div>
    </form>
</div>