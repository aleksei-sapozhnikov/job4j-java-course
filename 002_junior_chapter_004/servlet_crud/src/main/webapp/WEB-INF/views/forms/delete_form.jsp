<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="context" scope="request" value="${pageContext.request.contextPath}"/>
<%--@elvariable id="roles" type="java.util.Collection"--%>
<%--@elvariable id="users" type="java.util.List<ru.job4j.crud.model.User>"--%>

<div class="container-fluid">
    <h2>Update user</h2>
    <form class="form-horizontal" id="user_form"
          action="${context}${initParam.delete}" method="POST">
        <!-- Id of user to delete -->
        <div class="form-group">
            <label class="control-label col-sm-2" for="id">Id:</label>
            <div class="col-sm-10">
                <select name="id" class="form-control" id="id">
                    <c:forEach items="${users}" var="user">
                        <option value="${user.id}">
                                ${user.id}
                        </option>
                    </c:forEach>
                </select>
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