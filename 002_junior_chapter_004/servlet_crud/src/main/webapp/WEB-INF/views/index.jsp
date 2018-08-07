<%--@elvariable id="roles" type="java.util.List<Role>"--%>
<%--@elvariable id="loggedUser" type="ru.job4j.crud.model.User"--%>
<%--@elvariable id="users" type="java.util.List<ru.job4j.crud.model.User>"--%>
<%--@elvariable id="error" type="java.lang.String"--%>
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

    <!-- Bootbox - for confirmations -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootbox.js/4.4.0/bootbox.min.js"></script>

    <!-- Bootstrap -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

    <style>
        body {
            padding-top: 70px;
        }
    </style>

    <script>
        /**
         * Запретить дефолтное поведение
         * при нажатии на кнопку "Create user"
         */
        $(document).ready(function () {
            $("#user-create-button").button().on("click", function (event) {
                event.preventDefault();
            });
        });

        /**
         * Запретить дефолтное поведение
         * при нажатии на кнопку "Update user"
         */
        $(document).ready(function () {
            $("#user-update-button").button().on("click", function (event) {
                event.preventDefault();
            });
        });

        $(document).ready(function () {
            $('#user-update-dialog').on('show.bs.modal', function () {
                var select = $('#user-update-select-id');
                getIdsAndWriteHtmlToIdSelect(select);
                getUserAndSetInputValues($('#user-update-form'), select.val());
            })
        });

        $(document).ready(function () {
            $(".update-specific-user-button").button().on("click", function (event) {
                event.preventDefault();
                var updateId = $(this).parent().find(":input[name=id]").val();
                var select = $('#user-update-select-id');
                getIdsAndWriteHtmlToIdSelect(select);
                getUserAndSetInputValues($('#user-update-form'), updateId);
            });
        });

        /**
         * Событие при сабмите формы CREATE пользователя
         */
        $(document).ready(function () {
            $('#user-update-select-id').change(function () {
                var id = $(this).val();
                getUserAndSetInputValues($('#user-update-form'), id);
            });
        });

        function getUserAndSetInputValues(form, id) {
            $.ajax({
                type: 'POST',
                url: "${context}${initParam.users}",
                data: JSON.stringify({
                    id: id
                }),
                success: function (response) {
                    setUpdateFormValues(form, response);
                }
            });
        }

        function getIdsAndWriteHtmlToIdSelect(selectObj) {
            $.ajax({
                type: 'POST',
                url: "${context}${initParam.selectors}",
                data: JSON.stringify({
                    request: 'ids'
                }),
                success: function (response) {
                    writeHtmlToIdSelector(selectObj, response);
                }
            });
        }

        function writeHtmlToIdSelector(selector, ids) {
            var html = "";
            for (var i = 0; i < ids.length; i++) {
                html += "<option value=\"" + ids[i] + "\">" + ids[i] + "</option>";
            }
            selector.html(html);
        }

        function setUpdateFormValues(form, user) {
            $(form).find(':input[name=id]').val(user.id);
            $(form).find(':input[name=login]').val(user.credentials.login);
            $(form).find(':input[name=password]').val(user.credentials.password);
            $(form).find(':input[name=role]').val(user.credentials.role);
            $(form).find(':input[name=name]').val(user.info.name);
            $(form).find(':input[name=email]').val(user.info.email);
            $(form).find(':input[name=country]').val(user.info.country);
            $(form).find(':input[name=city]').val(user.info.city);
        }

        /**
         * Событие при сабмите формы CREATE пользователя
         */
        $(document).ready(function () {
            $('#user-create-form').submit(function () {
                addUser(this);
                return false;
            });
        });

        /**
         * Событие при сабмите формы UPDATE пользователя
         */
        $(document).ready(function () {
            $('#user-update-form').submit(function () {
                var id = $(this).find(':input[name=id]').val();
                updateUser(id, this);
                return false;
            });
        });

        /**
         * Formats user created time into needed format
         * and returns that string.
         */
        function formatDateTime(milliseconds) {
            var obj = new Date(milliseconds);
            var date = $.datepicker.formatDate('dd.mm.yy', obj);
            var time = obj.getHours() + ':' + obj.getMinutes() + ':' + obj.getSeconds();
            return date + ' ' + time;
        }

        function getValues(form) {
            return {
                login: $(form).find(':input[name=login]').val(),
                password: $(form).find(':input[name=password]').val(),
                role: $(form).find(':input[name=role]').val(),
                name: $(form).find(':input[name=name]').val(),
                email: $(form).find(':input[name=email]').val(),
                country: $(form).find(':input[name=country]').val(),
                city: $(form).find(':input[name=city]').val()
            };
        }

        function addRow(table, user) {
            $(table).find("tbody").append(
                "<tr id=\"users-row-" + user.id + "\">" +
                "<td  id=\"users-row-" + user.id + "-id-created\">" +
                "<div><b>Id: </b>" + user.id + "</div>" +
                "<div><b>Created: </b>" + formatDateTime(user.created) + "</div>" +
                "</td>" +
                "<td  id=\"users-row-" + user.id + "-credentials\">" +
                "<div><b>Login: </b>" + user.credentials.login + "</div>" +
                "<div><b>Role: </b>" + user.credentials.role + "</div>" +
                "</td>" +
                "<td  id=\"users-row-" + user.id + "-info\">" +
                "<div><b>Name: </b>" + user.info.name + "</div>" +
                "<div><b>Email: </b>" + user.info.email + "</div>" +
                "<div><b>Country: </b>" + user.info.country + "</div>" +
                "<div><b>City: </b>" + user.info.city + "</div>" +
                "</td>" +
                "<td>" +
                "<div class=\"row\">" +
                "<div class=\"col-sm-6\">" +
                "<form method=\"post\">" +
                "<input name=\"id\" type=\"hidden\" value=\"" + user.id + "\"/>" +
                "<input type=\"button\" class=\"btn btn-primary\" data-toggle=\"modal\" " +
                "data-target=\"#user-update-dialog\"  value=\"Update\" " +
                "onclick=\"updateUser(" + user.id + ")\"/>" +
                "</form>" +
                "</div>" +
                "<div class=\"col-sm-6\">" +
                "<form method=\"post\">" +
                "<input name=\"id\" type=\"hidden\" value=\"" + user.id + "\"/>" +
                "<input type=\"button\" class=\"btn btn-primary\"  value=\"Delete\" " +
                "onclick=\"beginDeleteUser(" + user.id + ")\"/>" +
                "</form>" +
                "</div>" +
                "</div>" +
                "</td>" +
                "</tr>");
            return false;
        }

        function handleCreateUserResponse(response) {
            if (response.id !== -1) {
                addRow($("#users"), response);
                $('#user-create-dialog').modal('toggle'); // close dialog
            }
            else {
                alert('User create failed');
            }
        }

        function addUser(form) {
            var user = getValues(form);
            $.ajax({
                type: 'POST',
                url: "${context}${initParam.create}",
                data: JSON.stringify(user),
                success: function (response) {
                    handleCreateUserResponse(response);
                }
            });
        }

        /**
         * Подтверждает удаление пользователя и
         * запускает удаление
         */
        function beginDeleteUser(id) {
            bootbox.confirm("Delete user (id=" + id + ")?", function (result) {
                if (result) {
                    deleteUser(id)
                }
            });
        }

        /**
         * Удаляет пользователя по id.
         */
        function deleteUser(id) {
            $.ajax({
                type: 'POST',
                url: "${context}${initParam.delete}",
                data: JSON.stringify(id),
                success: function (response) {
                    handleDeleteUserResponse(response);
                }
            });
        }

        /**
         * Handles server response to delete user request.
         */
        function handleDeleteUserResponse(response) {
            if (response.id !== -1) {
                deleteRow(response.id);
            }
            else {
                alert('User delete failed');
            }
        }

        /**
         * Удаляет ряд с пользователем по id
         * из таблицы пользователей.
         */
        function deleteRow(id) {
            var rowName = '#users-row-' + id;
            $(rowName).remove();
        }

        /**
         * Функция для обновления пользователя.
         */
        function updateUser(id, form) {
            var update = getValues(form);
            $.ajax({
                type: 'POST',
                url: "${context}${initParam.update}",
                data: JSON.stringify({
                    id: id,
                    update: update
                }),
                success: function (response) {
                    handleUpdateUserResponse(response);
                }
            });
        }

        function handleUpdateUserResponse(response) {
            if (response.id !== -1) {
                updateRow(response);
                $('#user-update-dialog').modal('toggle'); // close dialog
            }
            else {
                alert('User update failed');
            }
        }

        function updateRow(user) {
            var rowNameRoot = '#users-row-' + user.id;
            var rowNameIdCreated = rowNameRoot + '-id-created';
            var rowNameCredentials = rowNameRoot + '-credentials';
            var rowNameInfo = rowNameRoot + '-info';

            var rowIdCreated = "<div><b>Id: </b>" + user.id + "</div>" +
                "<div><b>Created: </b>" + formatDateTime(user.created) + "</div>";
            var rowCredentials = "<div><b>Login: </b>" + user.credentials.login + "</div>" +
                "<div><b>Role: </b>" + user.credentials.role + "</div>";
            var rowInfo = "<div><b>Name: </b>" + user.info.name + "</div>" +
                "<div><b>Email: </b>" + user.info.email + "</div>" +
                "<div><b>Country: </b>" + user.info.country + "</div>" +
                "<div><b>City: </b>" + user.info.city + "</div>";

            $(rowNameIdCreated).html(rowIdCreated);
            $(rowNameCredentials).html(rowCredentials);
            $(rowNameInfo).html(rowInfo);
        }

        // $(function () {
        //     /**
        //      * Задание переменных и регулярных выражений.
        //      * From http://www.whatwg.org/specs/web-apps/current-work/multipage/states-of-the-type-attribute.html#e-mail-state-%28type=email%29
        //      *
        //      * В том числе есть переменная allfields - все поля пользователя.
        //      */
        //     var
        //         emailRegex = /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/,
        //         login = $("#login"),
        //         password = $("#password"),
        //         role = $("#role"),
        //         name = $("#name"),
        //         email = $("#email"),
        //         country = $("#country"),
        //         city = $("#city"),
        //
        //         allFields = $([]).add(login).add(password).add(role).add(name).add(email).add(country).add(city),
        //         tips = $(".validateTips");
        //
        //     /**
        //      * Подсветка подсказки желтым цветом, исчезает через 1,5 секунды
        //      */
        //     function updateTips(t) {
        //         tips
        //             .text(t)
        //             .addClass("ui-state-highlight");
        //         setTimeout(function () {
        //             tips.removeClass("ui-state-highlight", 1500);
        //         }, 500);
        //     }
        //
        //     /**
        //      * Проверка длины полz
        //      * o - объект input, n - имя объекта input
        //      * min/max - минимальная/максимальная допустимая длина
        //      *
        //      * Если длина не соответствует - добавляем в update-tips запись, которую
        //      * покажем пользователю.
        //      */
        //     function checkLength(o, n, min, max) {
        //         if (o.val().length > max || o.val().length < min) {
        //             o.addClass("ui-state-error");
        //             updateTips("Length of " + n + " must be between " +
        //                 min + " and " + max + ".");
        //             return false;
        //         } else {
        //             return true;
        //         }
        //     }
        //
        //     /**
        //      * Проверка на соответствие регулярному выражению.
        //      * o - объект (input), regexp - рег. выражение, имя объекта input.
        //      */
        //     function checkRegexp(o, regexp, n) {
        //         if (!(regexp.test(o.val()))) {
        //             o.addClass("ui-state-error");
        //             updateTips(n);
        //             return false;
        //         } else {
        //             return true;
        //         }
        //     }
        //
        //     /**
        //      * Добавление нового пользователя.
        //      */
        //     function addUser() {
        //         // очищаем сообщение об ошибках
        //         var valid = true;
        //         allFields.removeClass("ui-state-error");
        //
        //
        //         // проверяем валидность. После первой же ошибки - остальные проверки просто пропускаются.
        //         valid = valid && checkLength(login, "login", 3, 16);
        //         valid = valid && checkLength(password, "password", 5, 16);
        //         valid = valid && checkLength(name, "username", 3, 16);
        //         valid = valid && checkLength(email, "email", 6, 80);
        //         valid = valid && checkLength(country, "country", 1, 50);
        //         valid = valid && checkLength(city, "city", 1, 50);
        //
        //         valid = valid && checkRegexp(name, /^[a-z]([0-9a-z_\s])+$/i, "Username may consist of a-z, 0-9, underscores, spaces and must begin with a letter.");
        //         valid = valid && checkRegexp(email, emailRegex, "eg. ui@jquery.com");
        //         valid = valid && checkRegexp(password, /^([0-9a-zA-Z])+$/, "Password field only allow : a-z 0-9");
        //
        //         // Если все прошло успешно - добавляем пользователя
        //         if (valid) {
        //             $("#users").find("tbody").append("<tr>" +
        //                 "<td>" + "id=xz" + "created=xz_too" + "</td>" +
        //                 "<td>" + 'login=' + login.val() + ' role=' + role.val() + "</td>" +
        //                 "<td>" + "name=" + name.val() + " email=" + email.val() +
        //                 "country=" + country.val() + " city=" + city.val() + "</td>" +
        //                 "</tr>");
        //             $('#dialog-form').modal('toggle');
        //         }
        //         // Возвращаем - прошло все хорошо или нет.
        //         return valid;
        //     }
        //
        //     /**
        //      * Событие при сабмите формы
        //      */
        //     $("#user-create-form").on("submit", function (event) {
        //         event.preventDefault();
        //         alert("FormId=" + formID + ", formName=" + formNm);
        //         addUser();
        //     });

        // });
    </script>
</head>
<body>

<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container-fluid">
        <form class="navbar-form navbar-left" action="${context}" method="GET">
            <button class="btn btn-primary navbar-btn">Home</button>
        </form>
        <form class="navbar-form navbar-left">
            <button id="user-create-button" class="btn btn-primary navbar-btn"
                    data-toggle="modal" data-target="#user-create-dialog">Create user
            </button>
        </form>
        <form class="navbar-form navbar-left">
            <button id="user-update-button" class="btn btn-primary navbar-btn"
                    data-toggle="modal" data-target="#user-update-dialog">Update user
            </button>
        </form>
        <c:if test="${loggedUser != null}">
            <div class="nav navbar-nav navbar-right">
                <div class="row">
                    <div class="col-sm-8">
                        <p class="navbar-text">Logged:&nbsp${loggedUser.info.name},
                            id:&nbsp${loggedUser.id}, role:&nbsp${loggedUser.credentials.role}</p>

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

<!-- User create dialog -->
<div id="user-create-dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <!-- Title and close button -->
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">Create new user</h4>
            </div>
            <form id="user-create-form" class="form-horizontal">
                <div class="modal-body">
                    <p class="validateTips">All form fields are required.</p>
                    <!-- Login -->
                    <div class="form-group">
                        <label class="control-label col-sm-2">Login:</label>
                        <div class="col-sm-10">
                            <input name="login" type="text" class="form-control"
                                   placeholder="Enter login (e.g. nagibator2000)">
                        </div>
                    </div>
                    <!-- Password -->
                    <div class="form-group">
                        <label class="control-label col-sm-2">Password:</label>
                        <div class="col-sm-10">
                            <input name="password" type="password" class="form-control"
                                   placeholder="Enter password (e.g. qwerty123)">
                        </div>
                    </div>
                    <!-- Role -->
                    <div class="form-group">
                        <label class="control-label col-sm-2">Role:</label>
                        <div class="col-sm-10">
                            <select name="role" class="form-control">
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
                        <label class="control-label col-sm-2">Name:</label>
                        <div class="col-sm-10">
                            <input name="name" type="text" class="form-control"
                                   placeholder="Enter name (e.g. John Sullivan)">
                        </div>
                    </div>
                    <!-- Email -->
                    <div class="form-group">
                        <label class="control-label col-sm-2">Email:</label>
                        <div class="col-sm-10">
                            <input name="email" type="email" class="form-control"
                                   placeholder="Enter email (e.g. sullivan@netmail.com)">
                        </div>
                    </div>
                    <!-- Country -->
                    <div class="form-group">
                        <label class="control-label col-sm-2">Country:</label>
                        <div class="col-sm-10">
                            <input name="country" type="text" class="form-control"
                                   placeholder="Enter country (e.g. Russia)">
                        </div>
                    </div>
                    <!-- City -->
                    <div class="form-group">
                        <label class="control-label col-sm-2">City:</label>
                        <div class="col-sm-10">
                            <input name="city" type="text" class="form-control"
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

<!-- User update dialog -->
<div id="user-update-dialog" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <!-- Title and close button -->
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">Update user</h4>
            </div>
            <form id="user-update-form" class="form-horizontal">
                <div class="modal-body">
                    <p class="validateTips">All form fields are required.</p>
                    <!-- Id of user to update -->
                    <div class="form-group">
                        <label class="control-label col-sm-2">Choose id:</label>
                        <div class="col-sm-10">
                            <select id="user-update-select-id" name="id" class="form-control">
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
                        <label class="control-label col-sm-2">Login:</label>
                        <div class="col-sm-10">
                            <input name="login" type="text" class="form-control"
                                   placeholder="Enter login (e.g. nagibator2000)">
                        </div>
                    </div>
                    <!-- Password -->
                    <div class="form-group">
                        <label class="control-label col-sm-2">Password:</label>
                        <div class="col-sm-10">
                            <input name="password" type="password" class="form-control"
                                   placeholder="Enter password (e.g. qwerty123)">
                        </div>
                    </div>
                    <!-- Role -->
                    <div class="form-group">
                        <label class="control-label col-sm-2">Role:</label>
                        <div class="col-sm-10">
                            <select name="role" class="form-control">
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
                        <label class="control-label col-sm-2">Name:</label>
                        <div class="col-sm-10">
                            <input name="name" type="text" class="form-control"
                                   placeholder="Enter name (e.g. John Sullivan)">
                        </div>
                    </div>
                    <!-- Email -->
                    <div class="form-group">
                        <label class="control-label col-sm-2">Email:</label>
                        <div class="col-sm-10">
                            <input name="email" type="email" class="form-control"
                                   placeholder="Enter email (e.g. sullivan@netmail.com)">
                        </div>
                    </div>
                    <!-- Country -->
                    <div class="form-group">
                        <label class="control-label col-sm-2">Country:</label>
                        <div class="col-sm-10">
                            <input name="country" type="text" class="form-control"
                                   placeholder="Enter country (e.g. Russia)">
                        </div>
                    </div>
                    <!-- City -->
                    <div class="form-group">
                        <label class="control-label col-sm-2">City:</label>
                        <div class="col-sm-10">
                            <input name="city" type="text" class="form-control"
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
            <tr id="users-row-${user.id}">
                <td id="users-row-${user.id}-id-created">
                    <div><b>Id: </b>${user.id}</div>
                    <div>
                        <b>Created: </b>
                        <jsp:setProperty name="dateTime" property="time" value="${user.created}"/>
                        <fmt:formatDate value="${dateTime}" pattern="dd.MM.yyyy HH:mm:ss"/>
                    </div>
                </td>
                <td id="users-row-${user.id}-credentials">
                    <div><b>Login: </b>${user.credentials.login}</div>
                    <div><b>Role: </b>${user.credentials.role}</div>
                </td>
                <td id="users-row-${user.id}-info">
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
                                <input type="button" class="btn btn-primary update-specific-user-button"
                                       data-toggle="modal" data-target="#user-update-dialog" value="Update"/>
                            </form>
                        </div>
                        <div class="col-sm-6">
                            <form action="${context}${initParam.update}" method="post">
                                <input name="id" type="hidden" value="${user.id}"/>
                                <input id="delete_${user.id}" type="button" class="btn btn-primary" value="Delete"
                                       onclick="beginDeleteUser(${user.id})"/>
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