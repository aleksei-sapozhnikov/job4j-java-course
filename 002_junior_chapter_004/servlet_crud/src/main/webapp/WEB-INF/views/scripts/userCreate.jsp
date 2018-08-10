<%--@elvariable id="context" type="java.lang.String"--%>
<script>

    function addUser(form) {
        var user = getValues(form);
        if (isValidUser(user)) {
            $.ajax({
                type: 'POST',
                url: "${context}${initParam.create}",
                data: JSON.stringify(user),
                success: function (response) {
                    handleCreateUserResponse(response);
                }
            });
        }
    }

    function handleCreateUserResponse(response) {
        if (response.id !== -1) {
            addRow($("#users"), response);
            $('#user-create-dialog').modal('toggle'); // close dialog
        }
        else {
            alert('User create failed on server-side');
        }
    }

    function addRow(table, user) {
        $(table).find("tbody").append(
            "<tr id=\"users-row-" + user.id + "\">" +
            "<td  id=\"users-row-" + user.id + "-id-created\">" +
            "<div><b>Id: </b>" + user.id + "</div>" +
            "<div><b>Created: </b>" + formatDateTime(user.created) + "</div>" +
            "</td>" +
            "<td  id=\"users-row-" + user.id + "-credentials\">" +
            "<div><b>Login: </b>" + user.credentials.login + "</div>" +
            "<div><b>Role: </b>" + user.credentials.role + "</div>" +
            "</td>" +
            "<td  id=\"users-row-" + user.id + "-info\">" +
            "<div><b>Name: </b>" + user.info.name + "</div>" +
            "<div><b>Email: </b>" + user.info.email + "</div>" +
            "<div><b>Country: </b>" + user.info.country + "</div>" +
            "<div><b>City: </b>" + user.info.city + "</div>" +
            "</td>" +
            "<td>" +
            "<div class=\"row\">" +
            "<div class=\"col-sm-6\">" +
            "<form method=\"post\">" +
            "<input name=\"id\" type=\"hidden\" value=\"" + user.id + "\"/>" +
            "<input type=\"button\" class=\"btn btn-primary\" data-toggle=\"modal\" " +
            "data-target=\"#user-update-dialog\"  value=\"Update\" " +
            "onclick=\"updateUserFormFill(" + user.id + ")\"/>" +
            "</form>" +
            "</div>" +
            "<div class=\"col-sm-6\">" +
            "<form method=\"post\">" +
            "<input name=\"id\" type=\"hidden\" value=\"" + user.id + "\"/>" +
            "<input type=\"button\" class=\"btn btn-primary\"  value=\"Delete\" " +
            "onclick=\"beginDeleteUser(" + user.id + ")\"/>" +
            "</form>" +
            "</div>" +
            "</div>" +
            "</td>" +
            "</tr>");
        return false;
    }
</script>
