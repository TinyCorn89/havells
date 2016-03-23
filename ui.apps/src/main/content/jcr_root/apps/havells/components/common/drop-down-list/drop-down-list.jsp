<%@ page import="com.havells.core.model.AnalystCoverageComponent" %>
<%@include file="/apps/havells/global.jsp" %>
<c:set var="links" value="<%=new AnalystCoverageComponent(resource)%>"/>
<div class="formElementsWrapper">
    <div class="fieldWrap">
        <select class="select city" id="my_selection">
            <option> Select Location</option>
            <c:forEach items="${links.fields}" var="item" varStatus="loop">
                <c:set var="urlPath" value="${item.fieldWidth}"/>
                <c:set var="extension" value='${(not fn:endsWith(urlPath, ".html") )? ".html": "" }'/>
                <option data-href="${item.fieldWidth}${extension}">${item.fieldName}</option>
            </c:forEach>
        </select>
    </div>
</div>
<script>
    $(document).ready(function()
    {  document.getElementById('my_selection').onchange = function () {
        window.location.href = this.children[this.selectedIndex].getAttribute('data-href');
    }  });
    $(window).bind("load", function() {
        $('#my_selection option[data-href="'+location.pathname+'"]').attr('selected','selected');
    });
</script>
