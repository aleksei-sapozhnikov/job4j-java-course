<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="row">${param.row}</c:set>
<c:set var="column">${param.column}</c:set>
<c:set var="price">${param.price}</c:set>

<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1, shrink-to-fit=no" name="viewport">

    <!-- Bootstrap CSS -->
    <link crossorigin="anonymous" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" rel="stylesheet">

    <title>Оплата забронированного местас</title>
</head>
<body>
<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.3.1.min.js"
        integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
<script crossorigin="anonymous" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
        src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
<script crossorigin="anonymous" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
        src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>

<script>
    const app_context = "${pageContext.request.contextPath}";

    $(function () {
        /**
         * Listener on 'go to main page' button. Goes to main page.
         */
        $("#go_to_main").click(function () {
            window.location.href = app_context;
        });
    });
</script>

<div class="container">
    <div class="row pt-3">
        <h3>
            Вы успешно оплатили
            <span style="color: green; "><c:out value="${price}"/></span> ₽
            и купили место:
            ряд <span style="color: red; "> <c:out value="${row}"/></span>,
            место <span style="color: red; "><c:out value="${column}"/></span>.
        </h3>
    </div>
    <div class="row float-right">
        <button id="go_to_main" type="button" class="btn btn-success">На главную</button>
    </div>


</div>

</body>
</html>
