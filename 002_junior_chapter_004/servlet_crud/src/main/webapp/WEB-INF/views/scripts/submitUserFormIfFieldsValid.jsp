<script>
    /**
     * Adds row to users table if user values are valid.
     * @returns {boolean} true if user was added, false if not.
     */
    function validateAndSubmit() {
        var result;
        var name = $('input[name=name]').val();
        var login = $('input[name=login]').val();
        var password = $('input[name=password]').val();
        var email = $('input[name=email]').val();
        var country = $('input[name=country]').val();
        var city = $('input[name=city]').val();
        var invalids = findInvalids(name, login, password, email, country, city);
        if (invalids.length === 0) {
            $('#user_form').submit();
            result = true;
        } else {
            alert("Values are invalid: " + invalids);
            result = false;
        }
        return result;
    }
</script>