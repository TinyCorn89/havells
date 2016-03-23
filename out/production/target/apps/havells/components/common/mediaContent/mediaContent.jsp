<%@ page import="java.util.UUID" %>
<%@include file="/apps/havells/global.jsp" %>
<%@page session="false" %>

<c:set var="filters" value="${properties.filter}"/>

<h1> ${properties.title} </h1>
<div class="rightWrapper">
        <input type="hidden" id="resourcePath" value="<%=resource.getPath()%>"/>
        <select class="yearColor">
            <c:forEach items="${filters}" var="current" varStatus="status">
                <option ${status.last ? "selected" : ""}>${current}</option>
            </c:forEach>
        </select>
</div>
<div id="mediaWrapper">
    <div id="contentListId" style="display:none">

    </div>
    <div id="noContentId" style="display:none"><b style='color:red'>no data found</b></div>
</div>

<div style="clear:both;"></div>
<script>
    $(document).ready(function () {
        var yearFilter = $('.yearColor');
        if(yearFilter !== 'undefined'){
            var resourcePath = $("#resourcePath").val() ;
            var path = resourcePath +".data."+$('.yearColor').val()+".1.html";
            callContent(path);
            $( ".yearColor" ).change(function() {
                var yearSelected =  $(this).val();
                callContent(resourcePath +".data."+yearSelected+".1.html");
            });
        }
    });
</script>
