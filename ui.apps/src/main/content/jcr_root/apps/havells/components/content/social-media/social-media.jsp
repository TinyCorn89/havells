
<%@include file="/apps/havells/global.jsp" %>
<%@ page import="java.util.UUID" %>
<%@ page import="com.havells.core.model.SocialMediaShareComponent" %>


<c:set var="social" value="<%= new SocialMediaShareComponent(resource) %>"/>

<input type="hidden" id="emailSubject" name="emailSubject" value="${properties.emailSubject}"/>
<input type="hidden" id="emailText" name="emailText" value="${properties.emailText}"/>

<li class="noBorderRight"><a href="javascript:;"><i class="fa fa-share-alt"></i><span>SHARE</span></a>
    <ul>
        <c:forEach items="${social.socialMediaNames}" var="media">
            <li><a target="_blank" ><i class="fa ${media}"></i></a></li>
        </c:forEach>
    </ul>
</li>

<c:set var="uuid" value="<%= UUID.randomUUID().toString()%>"/>
<div id="${uuid}"></div>

<script>
    jQuery(document).ready(function () {
        jQuery('#${uuid}').socialMediaLinks();
    });
</script>
