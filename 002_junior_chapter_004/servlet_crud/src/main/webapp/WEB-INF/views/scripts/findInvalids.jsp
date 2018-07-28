<script>
    /**
     * Returns all invalid fields.
     * @returns {object} true if valid, false if not.
     */
    function findInvalids(name, login, password, email, country, city) {
        var invalids = [];
        // check login
        if (login === "") {
            invalids.push("login");
        }
        // check password
        if (password === "") {
            invalids.push("password");
        }
        // check name
        if (name === "") {
            invalids.push("name");
        }
        // check email
        if (email === "" || !email.includes("@")) {
            invalids.push("email");
        }
        // check country
        if (country === "") {
            invalids.push("country");
        }
        // check city
        if (city === "") {
            invalids.push("city");
        }
        return invalids;
    }
</script>