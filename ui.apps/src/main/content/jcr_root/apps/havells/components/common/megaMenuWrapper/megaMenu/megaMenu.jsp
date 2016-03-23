<%@ page import="com.day.cq.wcm.api.PageFilter,
                 com.day.cq.wcm.api.components.IncludeOptions,
                 com.day.cq.wcm.foundation.Navigation" %>
<%@ page import="com.havells.core.model.product.ProductConstant" %>
<%@ page import="com.havells.util.RenditionUtil" %>
<%@ page import="java.util.Iterator" %>
<%@include file="/apps/havells/global.jsp" %>

<%
    //This code will only add the surrounding DIVs for the editbars when in EDIT mode only
    if (WCMMode.fromRequest(request) != WCMMode.EDIT) {
        IncludeOptions.getOptions(request, true).setDecorationTagName("");
    }
    boolean isEditMode = WCMMode.fromRequest(request) == WCMMode.EDIT || WCMMode.fromRequest(request) == WCMMode.DESIGN;
    String resourcePath = properties.get("parentUrl", "");
%>

<c:choose>
    <c:when test="${editMode==true}">
        <c:if test="${properties.leftCol=='Y'}">
            <cq:include path="megaMenuProfileInfo" resourceType="foundation/components/parsys"/>
        </c:if>
    </c:when>
    <c:otherwise>
        <%
            //This code will only add the surrounding DIVs for the editbars when in EDIT mode only
            IncludeOptions.getOptions(request, true).setDecorationTagName("");
        %>
        <% if (!resourcePath.equals("")) { %>

            <div class="subNavWrapper">
                <div class="container">
                    <div class="master">
                        <% }
                            Resource child = null;
                            Resource childRes = resource.getChild("megaMenuProfileInfo");
                            if (childRes != null) {
                                Iterator<Resource> childList = childRes.listChildren();
                                while (childList.hasNext()) {
                                    child = childList.next();
                                    break;
                                }
                                if (child != null) {

                        %>
                        <div class="aboutNavWrap">
                            <div class="left">
                                <sling:include path="<%= childRes.getPath() %>"/>
                            </div>
                            <div class="right">
                                <% }
                                } %>
                                <ul>
                                    <%
                                        int depth = properties.get("depth", 3);
                                        if (depth > 3 || depth < 0) {
                                            depth = 3;
                                        }
                                        if (!resourcePath.equals("")) {
                                            Resource res = resourceResolver.getResource(resourcePath);
                                            Page homePage = res.adaptTo(Page.class);
                                            PageFilter filter = new PageFilter(request);
                                            Navigation nav = new Navigation(homePage, homePage.getDepth() - 1, filter, depth);
                                            for (Navigation.Element e : nav) {
                                                switch (e.getType()) {
                                                    case NODE_OPEN:
                                                        out.println("<ul>");
                                                        break;
                                                    case ITEM_BEGIN:
                                                        out.println("<li>");
                                                        if (e.hasChildren()) {
                                                            out.println("<a href = '" + e.getPath() + ".html' class=''>" + e.getTitle() + "</a>");
                                                        } else {
                                                            Resource contentResource = e.getPage().getContentResource();
                                                            if (contentResource != null) {
                                                                Resource image = contentResource.getChild("image");
                                                                if (image != null) {
                                                                    String fileReference = RenditionUtil.getRendition(image.adaptTo(ValueMap.class).get("fileReference", ""),
                                                                            resourceResolver, ProductConstant.THUMBNAIL_RENDITION_150X143);
                                                                    if (!isEditMode) {
                                                                        out.println("<a href = '" + e.getPath() + ".html'>" + e.getTitle() + "</a>");
                                                                        out.println("<ul><li>");
                                                                        out.println("<img src='" + fileReference + "' style= 'max-height: 230px; max-width: 230px;' />");
                                                                        out.println("</li></ul>");
                                                                    }
                                                                } else {
                                                                    out.println("<a href = '" + e.getPath() + ".html' class=''>" + e.getTitle() + "</a>");
                                                                }
                                                            }
                                                        }
                                                        break;
                                                    case ITEM_END:
                                                        out.println("</li>");
                                                        break;
                                                    case NODE_CLOSE:
                                                        out.println("<li style='display:none'> <a class='viewAllLink' href='#'>View All</a></li>");
                                                        out.println("</ul>");
                                                        break;
                                                }
                                            }
                                        }
                                    %>
                                </ul>
                                <% if (child != null) { %>
                            </div>
                        </div>
                        <% }
                            if (!resourcePath.equals("")) { %>
                    </div>
                </div>
            </div>

        <%}%>
    </c:otherwise>
</c:choose>
