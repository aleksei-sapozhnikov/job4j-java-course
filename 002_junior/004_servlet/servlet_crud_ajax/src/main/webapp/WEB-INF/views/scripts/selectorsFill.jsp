<%--@elvariable id="context" type="java.lang.String"--%>
<script>
    /**
     * Gets ids of all users in database from server and call to
     * fill selector html by this ids.
     *
     * @param selectObj The "select" tag into where to write "option".
     * @param requestString String saying list of what we want to get from server.
     * html tags containing ids.
     */
    function getOptionsListAndWriteHtmlToSelect(selectObj, requestString) {
        $.ajax({
            type: 'POST',
            url: "${context}${initParam.selectors}",
            async: false,
            data: JSON.stringify({
                request: requestString
            }),
            success: function (response) {
                writeHtmlToSelector(selectObj, response);
            }
        });
    }

    /**
     * Gets list of countries of all users in database from server and call to
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
                writeHtmlToSelector(selectObj, response);
            }
        });
    }


    /**
     * Forms html code of "option" tags with values and writes it
     * into the given selector
     *
     * @param selector Selector to write html into.
     * @param values List of values to form "options" of.
     */
    function writeHtmlToSelector(selector, values) {
        var html = "";
        for (var i = 0; i < values.length; i++) {
            html += "<option value=\"" + values[i] + "\">" + values[i] + "</option>";
        }
        selector.html(html);
    }


</script>