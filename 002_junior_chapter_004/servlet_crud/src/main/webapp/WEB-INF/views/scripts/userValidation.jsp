<script>

    /**
     * Validation entry point.
     */
    function isValidUser(user) {
        var valid = true;
        valid = valid && checkLengthAll(user);
        valid = valid && checkRegexpAll(user);
        return valid;
    }

    function checkLengthAll(user) {
        var valid = true;
        valid = valid && checkLength(user.login, "login", 4, 10);
        valid = valid && checkLength(user.password, "password", 4, 10);
        valid = valid && checkLength(user.name, "name", 4, 10);
        valid = valid && checkLength(user.email, "email", 4, 10);
        valid = valid && checkLength(user.country, "country", 2, 20);
        valid = valid && checkLength(user.city, "city", 2, 20);
        return valid;
    }

    function checkRegexpAll(user) {
        var valid = true;
        valid = valid && checkRegexp(user.login, /^[a-z]([0-9a-z_\s])+$/i, "login");
        valid = valid && checkRegexp(user.name, /^[a-z]([0-9a-z_\s])+$/i, "name");
        valid = valid && checkRegexp(user.password, /^([0-9a-zA-Z])+$/, "password");
        return valid;
    }

    /**
     * Check if value has needed length.
     * If length is not ok - alerts message to user.
     *
     * @param value Value to check length of.
     * @param name How to call this value for user.
     * @param min Minimum length allowed.
     * @param max Maximum length allowed
     *
     * @return (boolean) true if length is ok, false if not.
     */
    function checkLength(value, name, min, max) {
        if (value.length > max || value.length < min) {
            alert("Length of " + name + " must be between " +
                min + " and " + max + ".");
            return false;
        } else {
            return true;
        }
    }


    /**
     * Checks if value fits needed regular expression.
     *
     * @param value Value to test.
     * @param regexp Regular expression.
     * @param name Name of the field for user.
     */
    function checkRegexp(value, regexp, name) {
        if (!(regexp.test(value))) {
            alert("Value of the " + name + " field doesn't correspond to regexp: " + regexp);
            return false;
        } else {
            return true;
        }
    }

</script>