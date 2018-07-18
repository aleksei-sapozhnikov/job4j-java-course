<script>
    function getValues(form) {
        var countryFromSelect = $(form).find(':input[name=country-select]').val();
        var countryFromForm = $(form).find(':input[name=country-form]').val();
        var cityFromSelect = $(form).find(':input[name=city-select]').val();
        var cityFromForm = $(form).find(':input[name=city-form]').val();
        return {
            login: $(form).find(':input[name=login]').val(),
            password: $(form).find(':input[name=password]').val(),
            role: $(form).find(':input[name=role]').val(),
            name: $(form).find(':input[name=name]').val(),
            email: $(form).find(':input[name=email]').val(),
            country: countryFromForm !== '' ? countryFromForm : countryFromSelect,
            city: cityFromForm !== '' ? cityFromForm : cityFromSelect
        };
    }
</script>