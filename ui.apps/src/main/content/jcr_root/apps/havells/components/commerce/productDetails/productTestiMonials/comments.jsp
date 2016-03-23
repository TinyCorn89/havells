<%@include file="/apps/havells/global.jsp"%>
<%@ page import="com.adobe.cq.commerce.api.CommerceException" %>
<%@ page import="com.adobe.cq.commerce.api.Product" %>
<%@ page import="com.havells.core.model.FetchTestimonials" %>
<%
    Product product = (Product)request.getAttribute("cq.commerce.product");
    String pPath = "";
    try{
        if(product != null && product.getPIMProduct() != null){
            pPath = product.getPIMProduct().getPath();
        }
    }catch (CommerceException ex){}
%>
<c:set var="filter" value="<%=request.getParameter("filter")%>"/>
<c:set var="productPath" value="<%=product != null ? pPath : request.getParameter("productPath")%>"/>
<c:set var="fetchTestimonials" value="<%=new FetchTestimonials(resource, pageContext.getAttribute("productPath") != null ? pageContext.getAttribute("productPath").toString() : "",request.getParameter("filter"))%>"/>
<c:set var="comments" value="${fetchTestimonials.comments}"/>

<div class="msg">
</div>
<ul>
    <c:forEach var="comment" items="${comments}">
        <li>
            <cq:include path="${comment.path}" resourceType="havells/components/commerce/productDetails/productTestiMonials/comment" />
        </li>
    </c:forEach>
</ul>
