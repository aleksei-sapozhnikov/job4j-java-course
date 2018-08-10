<script>
    /**
     * Waits for user to press "Create user" button.
     * Prevents default form submit in navigation bar form.
     */
    $(document).ready(function () {
        $("#user-create-button").button().on("click", function (event) {
            event.preventDefault();
        });
    });

    /**
     * Waits for user to press "Update user" button.
     * Prevents default form submit in navigation bar form.
     *
     * Calls to open and fill "UPDATE" user form.
     */
    $(document).ready(function () {
        $("#user-update-button").button().on("click", function (event) {
            event.preventDefault();
            updateUserFormFill($('#user-update-select-id').val())
        });
    });

    /**
     * When user selects another user to update, calls functions
     * to get selected user values from server and set them to inputs
     * in the "UPDATE" user form.
     */
    $(document).ready(function () {
        $('#user-update-select-id').change(function () {
            var id = $(this).val();
            getUserAndSetUpdateFormValues($('#user-update-form'), id);
        });
    });

    /**
     * When "CREATE USER" form submit happens,
     * calls function to add new user into database
     */
    $(document).ready(function () {
        $('#user-create-form').submit(function () {
            addUser(this);
            return false;
        });
    });

    /**
     * When "CREATE USER" form submit happens,
     * calls function to to update user.
     */
    $(document).ready(function () {
        $('#user-update-form').submit(function () {
            var id = $(this).find(':input[name=id]').val();
            updateUser(id, this);
            return false;
        });
    });
</script>