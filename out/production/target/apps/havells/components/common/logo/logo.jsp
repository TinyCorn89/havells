<%@include file="/apps/havells/global.jsp"%><%
%><%@ page import="com.day.text.Text,
                   com.day.cq.wcm.foundation.Image,
                   com.day.cq.commons.Doctype" %>
<%
    String logoResPath = globalPage + "/jcr:content/hpar/header/logo";
    String home = properties.get("homePage", "#");
	Resource logoRes = resource.getResourceResolver().getResource(logoResPath);

    if (logoRes == null) {
        logoRes = currentStyle.getDefiningResource("image");
    }
    %><a href="<%= xssAPI.getValidHref(home) %>.html"><%
    if (logoRes == null) {
        %>Home<%
    } else {
        Image img = new Image(logoRes);
        img.setItemName(Image.NN_FILE, "image");
        img.setItemName(Image.PN_REFERENCE, "imageReference");
        img.setSelector("img");
        img.setDoctype(Doctype.fromRequest(request));
        img.draw(out);
    }
%></a>
