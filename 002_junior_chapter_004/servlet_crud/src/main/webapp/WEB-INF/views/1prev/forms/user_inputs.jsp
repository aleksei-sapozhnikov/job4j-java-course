<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="roles" type="java.util.Collection"--%>

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
    <div class="col-sm-10">
        <input name="country" type="text" class="form-control" id="country"
               placeholder="Enter country (e.g. Russia)">
    </div>
</div>
<!-- City -->
<div class="form-group">
    <label class="control-label col-sm-2" for="city">City:</label>
    <div class="col-sm-10">
        <input name="city" type="text" class="form-control" id="city"
               placeholder="Enter city (e.g. Moscow)">
    </div>
</div>