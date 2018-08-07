<%--@elvariable id="context" type="java.lang.String"--%>
<script>
    /**
     * Функция для обновления пользователя.
     */
    function updateUser(id, form) {
        var update = getValues(form);
        $.ajax({
            type: 'POST',
            url: "${context}${initParam.update}",
            data: JSON.stringify({
                id: id,
                update: update
            }),
            success: function (response) {
                handleUpdateUserResponse(response);
            }
        });
    }

    function handleUpdateUserResponse(response) {
        if (response.id !== -1) {
            updateRow(response);
            $('#user-update-dialog').modal('toggle'); // close dialog
        }
        else {
            alert('User update failed');
        }
    }

    /**
     * Opens "UPDATE USER" form and fills it with needed values.
     *
     * @param updateId Id of the user to update.
     */
    function updateUserFormFill(updateId) {
        getIdsAndWriteHtmlToIdSelect($('#user-update-select-id'));
        getUserAndSetUpdateFormValues($('#user-update-form'), updateId);
    }

    /**
     * Gets selected user from server and calls function to set
     * user values to inputs in the "UPDATE" form.
     *
     * @param form Update form (object).
     * @param id Id of the selected user.
     */
    function getUserAndSetUpdateFormValues(form, id) {
        $.ajax({
            type: 'POST',
            url: "${context}${initParam.users}",
            async: false,
            data: JSON.stringify({
                id: id
            }),
            success: function (response) {
                setUpdateFormValues(form, response);
            }
        });
    }

    /**
     * Gets ids of all users in database from server and call to
     * fill selector html by this ids.
     *
     * @param selectObj The "select" tag into where to write "option"
     * html tags containing ids.
     */
    function getIdsAndWriteHtmlToIdSelect(selectObj) {
        $.ajax({
            type: 'POST',
            url: "${context}${initParam.selectors}",
            async: false,
            data: JSON.stringify({
                request: 'ids'
            }),
            success: function (response) {
                writeHtmlToIdSelector(selectObj, response);
            }
        });
    }

    /**
     * Forms html code of "option" tags with ids and writes it
     * into the given selector
     *
     * @param selector Selector to write html into.
     * @param ids List of ids to form "options" of.
     */
    function writeHtmlToIdSelector(selector, ids) {
        var html = "";
        for (var i = 0; i < ids.length; i++) {
            html += "<option value=\"" + ids[i] + "\">" + ids[i] + "</option>";
        }
        selector.html(html);
    }

    /**
     * Writes values of the given user into the given form values.
     *
     * @param form Form with inputs to be changed.
     * @param user User object with needed field values.
     */
    function setUpdateFormValues(form, user) {
        $(form).find(':input[name=id]').val(user.id);
        $(form).find(':input[name=login]').val(user.credentials.login);
        $(form).find(':input[name=password]').val(user.credentials.password);
        $(form).find(':input[name=role]').val(user.credentials.role);
        $(form).find(':input[name=name]').val(user.info.name);
        $(form).find(':input[name=email]').val(user.info.email);
        $(form).find(':input[name=country]').val(user.info.country);
        $(form).find(':input[name=city]').val(user.info.city);
    }

    function updateRow(user) {
        var rowNameRoot = '#users-row-' + user.id;
        var rowNameIdCreated = rowNameRoot + '-id-created';
        var rowNameCredentials = rowNameRoot + '-credentials';
        var rowNameInfo = rowNameRoot + '-info';

        var rowIdCreated = "<div><b>Id: </b>" + user.id + "</div>" +
            "<div><b>Created: </b>" + formatDateTime(user.created) + "</div>";
        var rowCredentials = "<div><b>Login: </b>" + user.credentials.login + "</div>" +
            "<div><b>Role: </b>" + user.credentials.role + "</div>";
        var rowInfo = "<div><b>Name: </b>" + user.info.name + "</div>" +
            "<div><b>Email: </b>" + user.info.email + "</div>" +
            "<div><b>Country: </b>" + user.info.country + "</div>" +
            "<div><b>City: </b>" + user.info.city + "</div>";

        $(rowNameIdCreated).html(rowIdCreated);
        $(rowNameCredentials).html(rowCredentials);
        $(rowNameInfo).html(rowInfo);
    }
</script>