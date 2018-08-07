<script>
    /**
     * Returns formatted date and time from milliseconds.
     */
    function formatDateTime(milliseconds) {
        var obj = new Date(milliseconds);
        var date = $.datepicker.formatDate('dd.mm.yy', obj);
        var time = getTimeUnitWithZeros(obj.getHours()) + ':' +
            getTimeUnitWithZeros(obj.getMinutes()) + ':' +
            getTimeUnitWithZeros(obj.getSeconds());
        return date + ' ' + time;
    }

    /**
     * Returns time unit value as string of two literals.
     * Adds preceeding zero if needed.
     */
    function getTimeUnitWithZeros(value) {
        return (value < 10 ? '0' : '') + value;
    }
</script>