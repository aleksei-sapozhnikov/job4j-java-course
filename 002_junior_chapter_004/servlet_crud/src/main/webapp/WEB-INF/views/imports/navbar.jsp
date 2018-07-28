<%--@elvariable id="const" type="ru.job4j.crud.Constants.GetterConstants"--%>
<%--@elvariable id="context" type="java.lang.String"--%>
<%--@elvariable id="create" type="java.lang.String"--%>
<%--@elvariable id="delete" type="java.lang.String"--%>
<%--@elvariable id="logout" type="java.lang.String"--%>
<%--@elvariable id="loggedUser" type="ru.job4j.crud.model.User"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Navigation bar -->
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <form class="navbar-form navbar-left" action="${context}" method="GET">
            <button type="submit" class="btn btn-primary navbar-btn">Home</button>
        </form>
        <form class="navbar-form navbar-left" action="${context}${create}" method="GET">
            <button type="submit" class="btn btn-primary navbar-btn">Create user</button>
        </form>
        <c:if test="${loggedUser != null}">
            <div class="nav navbar-nav navbar-right">
                <div class="row">
                    <div class="col-sm-8">
                        <p class="navbar-text">Logged: ${loggedUser.info.name} (id: ${loggedUser.id},
                            role: ${loggedUser.credentials.role})</p>
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
