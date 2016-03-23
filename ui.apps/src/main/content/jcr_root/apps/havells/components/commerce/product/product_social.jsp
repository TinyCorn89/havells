<%@page session="false"%>
<%@ include file="/libs/foundation/global.jsp" %>
<%@ page contentType="text/html; charset=utf-8" %>
<%
    String productRatingPath = (String) request.getAttribute("cq.commerce.productTrackingPath");
    productRatingPath = trimHashAndExtension(productRatingPath);
    String productPagePath = (String) request.getAttribute("cq.commerce.productPagePath");
    productPagePath = trimHashAndExtension(productPagePath);
%>
<div class="product-social">
    <div><cq:include path="<%= productRatingPath + "/jcr:content/rating"%>" resourceType="social/tally/components/rating"/></div>
    <div><cq:include path="<%= productPagePath + "/twitter_share" %>" resourceType="social/plugins/twitter/twittershare"/></div>
</div>
<%!
    String trimHashAndExtension(String url) {
      try{
        int i = url.indexOf("#");
        if (i > 0) {
            url = url.substring(0, i);
        }
        i = url.lastIndexOf(".");
        if (i > 0) {
            url = url.substring(0, i);
        }
      }catch (Exception ex){
      }
      return url;
    }
%>
