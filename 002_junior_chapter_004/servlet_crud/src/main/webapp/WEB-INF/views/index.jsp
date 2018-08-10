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

    <!-- Head common attrs -->
    <c:import url="head/head-common-attrs.jsp"/>

    <!-- Custom Scripts -->
    <c:import url="scripts/formatDateTime.jsp"/>
    <c:import url="scripts/pageActionsListeners.jsp"/>
    <c:import url="scripts/userCreate.jsp"/>
    <c:import url="scripts/userUpdate.jsp"/>
    <c:import url="scripts/userDelete.jsp"/>
    <c:import url="scripts/input.jsp"/>
    <c:import url="scripts/userValidation.jsp"/>

    <script>

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
        //        //
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

<!-- Navigation bar -->
<c:import url="page-parts/navbar.jsp"/>

<!-- User create dialog -->
<c:import url="page-parts/user-create-dialog.jsp"/>

<!-- User update dialog -->
<c:import url="page-parts/user-update-dialog.jsp"/>

<!-- Show all users table -->
<c:import url="page-parts/show-all-users-table.jsp"/>

</body>
</html>