<script>
    /**
     * Подтверждает удаление пользователя и
     * запускает удаление
     */
    function beginDeleteUser(id) {
        bootbox.confirm("Delete user (id=" + id + ")?", function (result) {
            if (result) {
                deleteUser(id)
            }
        });
    }

    /**
     * Удаляет пользователя по id.
     */
    function deleteUser(id) {
        $.ajax({
            type: 'POST',
            url: "${context}${initParam.delete}",
            data: JSON.stringify(id),
            success: function (response) {
                handleDeleteUserResponse(response);
            }
        });
    }

    /**
     * Handles server response to delete user request.
     */
    function handleDeleteUserResponse(response) {
        if (response.error !== null) {
            alert(response.error);
        } else {
            deleteRow(response.id);
        }
    }

    /**
     * Удаляет ряд с пользователем по id
     * из таблицы пользователей.
     */
    function deleteRow(id) {
        var rowName = '#users-row-' + id;
        $(rowName).remove();
    }
</script>