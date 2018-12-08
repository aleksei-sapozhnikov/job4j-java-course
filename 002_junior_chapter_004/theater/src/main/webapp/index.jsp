<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1, shrink-to-fit=no" name="viewport">

    <!-- Bootstrap CSS -->
    <link crossorigin="anonymous" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" rel="stylesheet">

    <title>Бронирование места на сеанс</title>
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
        /** Application context */
        const app_context = "${pageContext.request.contextPath}";
        /** Path to hall servlet */
        const app_hallServlet = app_context + '/hall_servlet';
        /** Path to payment page */
        const page_payment = app_context + '/payment.jsp';

        /**
         * Global variable: row and column chosen (checked by radiobutton) by user.
         * Is used when table of seats is updated automatically to check right seat on renewed table.
         * Also is used when user presses 'buy seat' button.
         */
        let chosen_rowCol = [-1, -1];
        /**
         * Global: list of all seats from storage.
         * Is used to form payment page to avoid one more request to server.
         */
        let all_seats;

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         * Functions to build and update table where user chooses places *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
        /**
         * Makes AJAX request of available places
         * and converts it to table of seats.
         */
        function fillPlacesTable() {
            $.ajax({
                type: 'GET',
                datatype: "text/html",
                url: app_hallServlet,
                success: function (response) {
                    if (response.error != null) {
                        alertDanger(response.error);
                    } else {
                        all_seats = response;
                        drawPlacesTable(all_seats);
                    }
                }
            });
        }

        /**
         * Forms html for the table with seats and writes it to needed block.
         * @param coordinates List of seat coordinates: [1, 1], [1, 2], [2, 1], ...
         */
        function drawPlacesTable(coordinates) {
            let html = '<h4>Бронирование места на сеанс</h4>';
            html += '<table id="place_choose" class="table table-bordered">';
            html += '<thead style="text-align: center;">';
            html += '<tr>';
            html += '<th style="width: 120px;">Ряд / Место</th>';
            for (let i = 1; i <= coordinates[0].length; i++) {
                html += '<th>' + i + '</th>';
            }
            html += '</thead>';
            html += '<tbody>';
            coordinates.forEach(function (row, i) {
                let seatRow = i + 1;
                html += "<tr>";
                html += '<th style="text-align: center;">';
                html += +seatRow;
                html += '</th>';
                row.forEach(function (seat, j) {
                    let seatColumn = j + 1;
                    let isFree = seat.free === true;
                    let doCheck = doCheckOrNot(seatRow, seatColumn, isFree);
                    html += '<td>';
                    html += '<label for="' + seatRow + ',' + seatColumn + '"></label>';
                    html += '<input name="place" ' +
                        'id="' + seatRow + ',' + seatColumn + '"' +
                        ' type="radio"' +
                        ' value="' + seatRow + ',' + seatColumn + '"' +
                        (isFree ? '' : ' disabled') +
                        (doCheck ? ' checked' : '') +
                        '>';
                    html += ' Ряд ' + seatRow + ', Место ' + seatColumn;
                    html += ' (';
                    html += seat.price + '₽, ';
                    html += isFree
                        ? '<span style="color: green; font-weight:bold; ">свободно' + '</span>'
                        : '<span style="color: red; font-weight:bold; ">занято' + '</span>';
                    html += ')';
                    html += '</td>';
                });
                html += "</tr>";
            });
            html += '</tbody>';
            html += "</table>";
            $("#table_choose_seat").html(html);
        }

        /**
         * Checks if we must check this seat (by radiobutton) or not.
         *
         * If seat was chosen but now can not be checked (because was occupied),
         * sets global variable of chosen seat to value of 'not-chosen seat'.
         *
         * @param seatRow Seat row.
         * @param seatColumn Seat Column.
         * @param isFree Boolean value showing if this seat is free or not (non-free seats can not be checked).
         *
         * @return Boolean value: true means 'checked', false - 'not checked'.
         */
        function doCheckOrNot(seatRow, seatColumn, isFree) {
            let result = false;
            let isChosen = seatRow === chosen_rowCol[0] && seatColumn === chosen_rowCol[1];
            if (isChosen && isFree) {
                result = true;
            } else if (isChosen && !isFree) {
                chosen_rowCol = [-1, -1];
            }
            return result;
        }

        /**
         * Listener on event:  when user checks radiobutton and chooses another place.
         */
        $("#table_choose_seat").on('change', $("input:radio[name='place']"), function () {
            let checked = $("input:radio[name='place']:checked").val().split(',');
            chosen_rowCol = [Number(checked[0]), Number(checked[1])];
        });

        /* * * * * * * * * * * * * * * * * * * * * * * *
         * Functions connected with booking operations *
         * * * * * * * * * * * * * * * * * * * * * * * */

        /**
         * Listener on event: when user chose place and presses the button to start
         * booking chosen place.
         */
        $("#begin_booking").click(function () {
            if (chosen_rowCol.every(v => v === -1)) {
                alertDanger('Выберите место');
            } else {
                getSelectedSeat()
            }
        });

        /**
         * Returns Seat object that user chose by radiobutton.
         */
        function getSelectedSeat() {
            let rowIndex = chosen_rowCol[0] - 1;
            let colIndex = chosen_rowCol[1] - 1;
            let seat = all_seats[rowIndex][colIndex];
            goToPaymentPage(seat);
        }

        /**
         * Goes tp payment page and transfers needed data.
         */
        function goToPaymentPage(seat) {
            window.location.href = page_payment +
                '?' + 'row=' + seat.row +
                '&' + 'column=' + seat.column +
                '&' + 'price=' + seat.price;
        }

        /* * * * * * * * * * *
         * Utility functions *
         * * * * * * * * * * */

        /**
         * Shows 'danger alert' with given message.
         * @param text Message.
         */
        function alertDanger(text) {
            $("#alert").html(
                '<div class="alert alert-danger alert-dismissible">' +
                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' +
                text +
                '</div>'
            );
        }

        /* * * * * * * * * * * * * * * * * * * *
         * Operations performed on page load.  *
         * * * * * * * * * * * * * * * * * * * */

        /**
         * Fill tables of seats.
         */
        fillPlacesTable();
        /**
         * Set table of seats update at interval.
         */
        setInterval(fillPlacesTable, 5000);


    });
</script>

<div class="container">
    <div id="alert"></div>

    <div id="table_choose_seat" class="row pt-3">
        <h4>Загружается таблица мест, подождите...</h4>
    </div>

    <div class="row float-right">
        <button id="begin_booking" class="btn btn-success" type="button">Оплатить</button>
    </div>
</div>

</body>
</html>