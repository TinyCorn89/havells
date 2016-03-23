<%@ page import="com.day.cq.wcm.api.PageFilter,
                 com.day.cq.wcm.foundation.Navigation,
                 java.util.Arrays,
                 java.util.List" %>
<%@ page import="com.day.cq.wcm.api.WCMMode,
                 com.day.cq.wcm.api.components.IncludeOptions" %>
<%@include file="/apps/havells/global.jsp" %>
<%
    //This code will only add the surrounding DIVs for the editbars when in EDIT mode only
    if (WCMMode.fromRequest(request) != WCMMode.EDIT && WCMMode.fromRequest(request) != WCMMode.DESIGN) {
        IncludeOptions.getOptions(request, true).setDecorationTagName("");
    }
%>
<div class="leftNavAccr listingSubLink">
    <div class="leftNavHeading"><%=properties.get("leftNavTitle", currentPage.getParent().getTitle())%></div>
    <div class="clearBoth"></div>
    <ul class="clearfix">
        <%
            String currentPagePath = currentPage.getPath();
            List<String> parents = Arrays.asList(currentPagePath.split("/"));
            String resourcePath = properties.get("parentUrl", currentPage.getParent().getPath());
            if (!resourcePath.equals("")) {
                int depth = properties.get("depth", 3);
                depth = (depth > 3 || depth < 0) ? 3 : depth;
                Resource res = resourceResolver.getResource(resourcePath);
                if (res != null) {
                    Page homePage = res.adaptTo(Page.class);
                    PageFilter filter = new PageFilter(request);
                    Navigation nav = new Navigation(homePage, homePage.getDepth() - 1, filter, depth);
                    String title = "";
                    boolean isTitleExist = false;
                    for (Navigation.Element e : nav) {
                        title = e.getPage().getName();
                        isTitleExist = parents.contains(title);
                        switch (e.getType()) {
                            case NODE_OPEN:
                                if (isTitleExist) {
                                    out.println("<ul style='display:block;'>");
                                } else {
                                    out.println("<ul>");
                                }
                                break;
                            case ITEM_BEGIN:
        %>
        <li class='<%= (isTitleExist)?"active" :"" %>'>

            <a href="<%= e.getPath()%>.html"><%= e.getTitle() %>
            </a>
                <%
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
        }
        %>
    </ul>
</div>
<c:if test="${!editMode}">
    <div style="clear:both;"></div>
</c:if>
