<%@include file="/apps/havells/global.jsp" %>
<input type="hidden" id="resourcePath" value="<%=resource.getPath()%>"/>
<div id="mediaWrapper">
    <div id="contentListId" style="display:none">
    </div>
    <div id="noContentId" style="display:none"><b style='color:red'>no data found</b></div>
</div>
<div class="clearBoth"></div>
<script>
    $(document).ready(function () {
        var resourcePath = $("#resourcePath").val() ;
        if(resourcePath != undefined)
        callContent(resourcePath +".data.1.html");
    });
</script>
