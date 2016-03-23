<%@include file="/apps/havells/global.jsp"%>
<div class="breadCrumbWrapper">
    <div class="container">
        <div class="master">
<%
    // get starting point of trail
    long level = currentStyle.get("absParent", 2L);
    long endLevel = currentStyle.get("relParent", 1L);
    String delimStr = currentStyle.get("delim", "&gt;&gt;");
    String trailStr = currentStyle.get("trail", "");
    int currentLevel = currentPage.getDepth();
    String delim = "&raquo;";
    while (level < currentLevel - endLevel) {
        Page trail = currentPage.getAbsoluteParent((int) level);
        if (trail == null) {
            break;
        }
        String title = trail.getNavigationTitle();
        if (title == null || title.equals("")) {
            title = trail.getNavigationTitle();
        }
        if (title == null || title.equals("")) {
            title = trail.getTitle();
        }
        if (title == null || title.equals("")) {
            title = trail.getName();
        }
        %><%
            if(title.equalsIgnoreCase(currentPage.getParent().getTitle())){ %>
                <span><%=currentPage.getParent().getTitle()%></span>
            <%}else{
                %><a href="<%= xssAPI.getValidHref(trail.getPath()+".html") %>"><%
                %><%= xssAPI.encodeForHTML(title) %><%
                %></a><% }
                //delim = delimStr;
                level++;
            if(level != currentLevel-1){%>
                <%= xssAPI.filterHTML(delim) %>
            <%}}
            if (trailStr.length() > 0) {
        %>
            <%= xssAPI.filterHTML(trailStr) %>
         <% } %>
        </div>
    </div>
</div>
