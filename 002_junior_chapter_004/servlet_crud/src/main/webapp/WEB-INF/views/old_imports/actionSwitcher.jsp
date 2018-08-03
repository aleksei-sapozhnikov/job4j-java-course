<!-- Action switcher -->
<div class="form-group" id="action">
    <label class="control-label col-sm-2">Action:</label>
    <div class="col-sm-10">
        <label class="radio-inline">
            <input type="radio" name="action" value="create"/>Create
        </label>
        <label class="radio-inline">
            <input type="radio" name="action" value="update"/>Update
        </label>
        <label class="radio-inline">
            <input type="radio" name="action" value="delete"/>Delete
        </label>
    </div>
</div>
<!-- Action form -->
<div id="action_form"></div>

<!-- Create user form -->
<div id="create_form" style="display:none">
    <c:import url="forms/create_form.jsp"/>
</div>
<!-- Update user form -->
<div id="update_form" style="display:none">
    <c:import url="forms/update_form.jsp"/>
</div>
<!-- Delete user form -->
<div id="delete_form" style="display:none">
    <c:import url="forms/delete_form.jsp"/>
</div>


<script>
    $(document).ready(function () {
        $('#action').on('change', function () {
            var action = $("[name=action]:checked").val();
            var form;
            switch (action) {
                case 'create':
                    form = 'create_form';
                    break;
                case 'update':
                    form = 'update_form';
                    fillValues($('#update_id').val());
                    break;
                case 'delete':
                    form = 'delete_form';
                    break;
            }
            changeForm(form);
        })
    });
</script>

<script>
    function changeForm(form) {
        document.getElementById('action_form').innerHTML =
            document.getElementById(form).innerHTML
    }
</script>