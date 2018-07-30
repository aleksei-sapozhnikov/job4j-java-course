<script>
    /**
     * Adds row to users table if user values are valid.
     * @returns {boolean} true if user was added, false if not.
     */
    function submitUserFormIfFieldsValid() {
        var result;
        var name = $('#name').val();
        var login = $('#login').val();
        var password = $("#password").val();
        var email = $('#email').val();
        var countryFromForm = $('#country_form').val();
        var country = countryFromForm === "" ? $('#country_ajax_list') : countryFromForm;
        var cityFromForm = $('#city_form').val();
        var city = cityFromForm === "" ? $('#city_ajax_list') : cityFromForm;
        alert("country=" + country + " city=" + city);
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