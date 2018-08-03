<%--@elvariable id="roles" type="java.util.List<Role>"--%>
<%--@elvariable id="loggedUser" type="ru.job4j.crud.model.User"--%>
<%--@elvariable id="users" type="java.util.List<ru.job4j.crud.model.User>"--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="dateTime" scope="request" class="java.util.Date"/>
<c:set var="context" scope="request" value="${pageContext.request.contextPath}"/>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>User list</title>

    <!-- Jquery -->
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

    <!-- Bootstrap -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

    <script>
        $(function () {
            var dialog,

                /**
                 * Задание переменных и регулярных выражений.
                 * From http://www.whatwg.org/specs/web-apps/current-work/multipage/states-of-the-type-attribute.html#e-mail-state-%28type=email%29
                 *
                 * В том числе есть переменная allfields - все поля пользователя.
                 */
                emailRegex = /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/,
                login = $("#login"),
                password = $("#password"),
                role = $("#role"),
                name = $("#name"),
                email = $("#email"),
                country = $("#country"),
                city = $("#city"),

                allFields = $([]).add(login).add(password).add(role).add(name).add(email).add(country).add(city),
                tips = $(".validateTips");

            /**
             * Подсветка подсказки желтым цветом, исчезает через 1,5 секунды
             */
            function updateTips(t) {
                tips
                    .text(t)
                    .addClass("ui-state-highlight");
                setTimeout(function () {
                    tips.removeClass("ui-state-highlight", 1500);
                }, 500);
            }

            /**
             * Проверка длины полz
             * o - объект input, n - имя объекта input
             * min/max - минимальная/максимальная допустимая длина
             *
             * Если длина не соответствует - добавляем в update-tips запись, которую
             * покажем пользователю.
             */
            function checkLength(o, n, min, max) {
                if (o.val().length > max || o.val().length < min) {
                    o.addClass("ui-state-error");
                    updateTips("Length of " + n + " must be between " +
                        min + " and " + max + ".");
                    return false;
                } else {
                    return true;
                }
            }

            /**
             * Проверка на соответствие регулярному выражению.
             * o - объект (input), regexp - рег. выражение, имя объекта input.
             */
            function checkRegexp(o, regexp, n) {
                if (!(regexp.test(o.val()))) {
                    o.addClass("ui-state-error");
                    updateTips(n);
                    return false;
                } else {
                    return true;
                }
            }

            /**
             * Добавление нового пользователя.
             */
            function addUser() {
                // очищаем сообщение об ошибках
                var valid = true;
                allFields.removeClass("ui-state-error");

                // проверяем валидность. После первой же ошибки - остальные проверки просто пропускаются.
                valid = valid && checkLength(login, "login", 3, 16);
                valid = valid && checkLength(password, "password", 5, 16);
                valid = valid && checkLength(name, "username", 3, 16);
                valid = valid && checkLength(email, "email", 6, 80);
                valid = valid && checkLength(country, "country", 1, 50);
                valid = valid && checkLength(city, "city", 1, 50);

                valid = valid && checkRegexp(name, /^[a-z]([0-9a-z_\s])+$/i, "Username may consist of a-z, 0-9, underscores, spaces and must begin with a letter.");
                valid = valid && checkRegexp(email, emailRegex, "eg. ui@jquery.com");
                valid = valid && checkRegexp(password, /^([0-9a-zA-Z])+$/, "Password field only allow : a-z 0-9");

                // Если все прошло успешно - добавляем пользователя
                if (valid) {
                    $("#users").find("tbody").append("<tr>" +
                        "<td>" + "id=xz" + "created=xz_too" + "</td>" +
                        "<td>" + 'login=' + login.val() + ' role=' + role.val() + "</td>" +
                        "<td>" + "name=" + name.val() + " email=" + email.val() +
                        "country=" + country.val() + " city=" + city.val() + "</td>" +
                        "</tr>");
                    $('#dialog-form').modal('toggle');
                }
                // Возвращаем - прошло все хорошо или нет.
                return valid;
            }

            /**
             * Формируем переменную для модального окна с диалогом.
             */
            dialog = $("#dialog-form");

            /**
             * Событие при сабмите формы
             */
            $("#create-user-form").on("submit", function (event) {
                event.preventDefault();
                addUser();
            });

            /**
             * Событие на кнопку "Create user"
             */
            $("#create-user").button().on("click", function (event) {
                $("#create-user").blur();
                event.preventDefault();
            });
        });
    </script>
</head>
<body>

<nav class="navbar navbar-default">
    <div class="container-fluid">
        <form class="navbar-form navbar-left" action="${context}" method="GET">
            <button class="btn btn-primary navbar-btn">Home</button>
        </form>
        <form class="navbar-form navbar-left">
            <button id="create-user" class="btn btn-primary navbar-btn"
                    data-toggle="modal" data-target="#dialog-form">Create user
            </button>
        </form>
        <c:if test="${loggedUser != null}">
            <div class="nav navbar-nav navbar-right">
                <div class="row">
                    <div class="col-sm-8">
                        <p class="navbar-text">Logged: ${loggedUser.info.name} (id: ${loggedUser.id},
                            role: ${loggedUser.credentials.role})</p>
                    </div>
                    <div class="col-sm-4">
                        <form class="navbar-form" action="${context}${initParam.logout}" method="POST">
                            <button type="submit" class="btn btn-primary navbar-btn">Logout</button>
                        </form>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</nav>

<!-- Bootstrap dialog form -->
<div id="dialog-form" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <!-- Title and close button -->
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">Create new user</h4>
            </div>
            <form id="create-user-form" class="form-horizontal">
                <div class="modal-body">
                    <p class="validateTips">All form fields are required.</p>
                    <!-- Login -->
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="login">Login:</label>
                        <div class="col-sm-10">
                            <input id="login" name="login" type="text" class="form-control"
                                   placeholder="Enter login (e.g. nagibator2000)">
                        </div>
                    </div>
                    <!-- Password -->
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="login">Password:</label>
                        <div class="col-sm-10">
                            <input id="password" name="password" type="password" class="form-control"
                                   placeholder="Enter password (e.g. qwerty123)">
                        </div>
                    </div>
                    <!-- Role -->
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="role">Role:</label>
                        <div class="col-sm-10">
                            <select id="role" name="role" class="form-control">
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
                            <input id="name" name="name" type="text" class="form-control"
                                   placeholder="Enter name (e.g. John Sullivan)">
                        </div>
                    </div>
                    <!-- Email -->
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="email">Email:</label>
                        <div class="col-sm-10">
                            <input id="email" name="email" type="email" class="form-control"
                                   placeholder="Enter email (e.g. sullivan@netmail.com)">
                        </div>
                    </div>
                    <!-- Country -->
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="country">Country:</label>
                        <div class="col-sm-10">
                            <input id="country" name="country" type="text" class="form-control"
                                   placeholder="Enter country (e.g. Russia)">
                        </div>
                    </div>
                    <!-- City -->
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="city">City:</label>
                        <div class="col-sm-10">
                            <input id="city" name="city" type="text" class="form-control"
                                   placeholder="Enter city (e.g. Moscow)">
                        </div>
                    </div>
                    <!-- Allow form submission with keyboard without duplicating the dialog button -->
                    <input type="submit" tabindex="-1" style="position:absolute; top:-1000px">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    <button type="submit" class="btn btn-primary">Save changes</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div id="users-contain" class="container-fluid">
    <h1>Existing Users:</h1>
    <table id="users" class="table table-hover">
        <thead>
        <tr>
            <th>Identity</th>
            <th>Credentials</th>
            <th>Info</th>
            <th align="center">Actions:</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${users}" var="user">
            <tr>
                <td>
                    <div><b>Id: </b>${user.id}</div>
                    <div>
                        <b>Created: </b>
                        <jsp:setProperty name="dateTime" property="time" value="${user.created}"/>
                        <fmt:formatDate value="${dateTime}" pattern="dd.MM.yyyy HH:mm:ss"/>
                    </div>
                </td>
                <td>
                    <div><b>Login: </b>${user.credentials.login}</div>
                    <div><b>Role: </b>${user.credentials.role}</div>
                </td>
                <td>
                    <div><b>Name: </b>${user.info.name}</div>
                    <div><b>Email: </b>${user.info.email}</div>
                    <div><b>Country: </b>${user.info.country}</div>
                    <div><b>City: </b>${user.info.city}</div>
                </td>
                <td>
                    <div class="row">
                        <div class="col-sm-6">
                            <form action="${context}${initParam.update}" method="post">
                                <input name="id" type="hidden" value="${user.id}"/>
                                <input id="update_${user.id}" type="button" class="btn btn-primary" value="Update"
                                       onclick="alert(this.id)"/>
                            </form>
                        </div>
                        <div class="col-sm-6">
                            <form action="${context}${initParam.update}" method="post">
                                <input name="id" type="hidden" value="${user.id}"/>
                                <input id="delete_${user.id}" type="button" class="btn btn-primary" value="Delete"
                                       onclick="alert(this.id)"/>
                            </form>
                        </div>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

</body>
</html>