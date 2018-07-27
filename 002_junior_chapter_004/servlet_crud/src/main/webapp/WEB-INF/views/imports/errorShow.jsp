<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Error messages show -->
<c:if test="${param.error != ''}">
    <div class="text-center">
        <div class="alert alert-danger">
            <strong>Error!</strong> ${param.error}
        </div>
    </div>
</c:if>
