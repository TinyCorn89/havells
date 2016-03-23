<%@include file="/apps/havells/global.jsp" %>

<c:set var="selector" scope="page" value="<%= slingRequest.getRequestPathInfo().getSelectors()%>"/>
<div class="uploadProductData">

    <h2 style="color:blue">Welcome to Uploading Product Data From </h2>
    <c:choose>
        <c:when test="${selector[0] eq 'success'}">
            <b>File has been successfully uploaded.<br/>Add Another...</b>
        </c:when>
        <c:otherwise>
            <b>To upload product data from XLSX file...</b>
        </c:otherwise>
    </c:choose>

    <form name="uploadProductDataForm" action="${resource.path}.submit.html" method="POST" id="uploadProductDataForm"
          enctype="multipart/form-data" novalidate="novalidate">
        <b style="color:red"></b>

        <div class="chooseFile">
            <label>Attach .xlsx file : </label>
            <input value="Choose File" type="file" id="fileData" name="fileData" class="chooseFileInner" required/>
        </div>
        <div class="chooseFile">
            <label for="techSpecsColumnNos">No. of Tech Spech columns in attached file :</label>
            <input name="techSpecsColumnNos" type="text" class="fieldInner" id="techSpecsColumnNos" required>
            <input name="currentPage" type="hidden" class="fieldInner" value="${currentPage.path}">
        </div>
        <div class="submit">
            <input type="submit" id="submitForm" value="Submit"/>
        </div>
    </form>
</div>

