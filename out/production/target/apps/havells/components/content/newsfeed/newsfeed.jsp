<%@include file="/libs/foundation/global.jsp" %>
<%@ page import="com.havells.core.model.NewsFeed" %>

<c:set var="newsfeeds" value="<%=new NewsFeed(resource)%>"/>
<c:set var="totalFeeds" value="${properties.totalFeeds}"/>
<div class="col newsAndUpdates">
    <div class="newsWrap">
        <h2>${properties.feedTitle}</h2>
        <ul>
            <c:forEach items="${newsfeeds.pressReleases}" begin="1" end="${totalFeeds}" varStatus="entry" var ="news">
                <li>
                        ${news.articleTitle}
                            <span>${news.date}</span>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>
