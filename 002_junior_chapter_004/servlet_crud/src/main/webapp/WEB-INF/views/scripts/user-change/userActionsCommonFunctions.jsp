<script>
    function getValues(form) {
        return {
            login: $(form).find(':input[name=login]').val(),
            password: $(form).find(':input[name=password]').val(),
            role: $(form).find(':input[name=role]').val(),
            name: $(form).find(':input[name=name]').val(),
            email: $(form).find(':input[name=email]').val(),
            country: $(form).find(':input[name=country]').val(),
            city: $(form).find(':input[name=city]').val()
        };
    }
</script>