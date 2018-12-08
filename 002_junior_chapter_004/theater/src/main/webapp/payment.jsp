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
    $(function () {
        const app_context = "${pageContext.request.contextPath}";
        const app_hallServlet = app_context + '/hall_servlet';
        const app_paymentSuccess = app_context + '/payment_success.jsp';

        /**
         * Listener on 'do payment' button click.
         */
        $("#do_payment").click(function () {
            if (isValidInput()) {
                processPayment();
            } else {
                alertDanger("Имя пользователя и телефон не могут быть пустыми");
            }

        });

        /**
         * Checks if input is valid.
         * @return {boolean} true, if input is valid, false if not.
         */
        function isValidInput() {
            return $("#username").val() !== ''
                && $("#phone").val() !== '';
        }

        /**
         * Processes payment operations.
         */
        function processPayment() {
            $.ajax({
                type: 'POST',
                datatype: 'application/json',
                url: app_hallServlet,
                data: JSON.stringify({
                    account_name: $("#username").val(),
                    account_phone: $("#phone").val(),
                    seat_row: ${row},
                    seat_column: ${column},
                }),
                success: function (response) {
                    if (response.error != null) {
                        alertDanger(response.error);
                    } else {
                        goToPaymentSuccessPage();
                    }
                }
            });
        }

        /**
         * Changes page to 'payment success' page.
         */
        function goToPaymentSuccessPage() {
            window.location.href = app_paymentSuccess +
                '?' + 'row=' + ${row} +
                    '&' + 'column=' + ${column} +
                    '&' + 'price=' + ${price};
        }

        /**
         * Listener on 'go to main page' button. Goes to main page.
         */
        $("#go_to_main").click(function () {
            window.location.href = app_context;
        });

        /**
         * Shows 'danger alert' with given message.
         * @param text Message.
         */
        function alertDanger(text) {
            $("#alert")
                .html(
                    '<div class="alert alert-danger alert-dismissible">' +
                    '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                    text +
                    '</div>'
                );
        }
    });
</script>

<div class="container">
    <div id="alert"></div>

    <div class="row pt-3">
        <button id="go_to_main" type="button" class="btn btn-success">На главную</button>
    </div>

    <div class="row pt-3">
        <h3>
            Вы выбрали:
            ряд <span style="color: red; "> <c:out value="${row}"/></span>,
            место <span style="color: red; "><c:out value="${column}"/></span>.
            К оплате:
            <span style="color: green; "><c:out value="${price}"/></span> ₽.
        </h3>
    </div>
    <div class="row">
        <form>
            <div class="form-group">
                <label for="username">ФИО</label>
                <input id="username" type="text" class="form-control" placeholder="ФИО">
            </div>
            <div class="form-group">
                <label for="phone">Номер телефона</label>
                <input id="phone" type="text" class="form-control" placeholder="Номер телефона">
            </div>
            <button id="do_payment" type="button" class="btn btn-success">Оплатить</button>
        </form>
    </div>
</div>

</body>
</html>
