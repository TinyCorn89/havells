<%@page session="false" %>
<%@ page import="com.havells.core.model.product.ProductCompare,
                 com.adobe.cq.commerce.api.CommerceService,
                 com.adobe.cq.commerce.api.CommerceSession" %>
<%@ page import="java.util.List" %>
<%@ page import="com.adobe.cq.commerce.api.Product" %>
<%@include file="/apps/havells/global.jsp" %>
<%
    try {
        CommerceService commerceService = resource.adaptTo(CommerceService.class);
        CommerceSession session = commerceService.login(slingRequest, slingResponse);
        ProductCompare productCompare = new ProductCompare(resource, request);
        List<Product> compareListProducts = productCompare.getProducts();
%>
<c:set var="compareListProducts" value="<%=compareListProducts%>"/>
<c:set var="techSpecMapList" value="<%=productCompare.getTechnicalSpecMap()%>"/>
<c:set var="productSize" value="<%=productCompare.getProductsSize()%>"/>
<c:choose>
    <c:when test="${productSize < 3}">
        <c:set var="tableDataWidth" value="33.33%"/>
        <c:set var="tableHeadingWidth" value="301%"/>
    </c:when>
    <c:otherwise>
        <c:set var="tableDataWidth" value="25%"/>
        <c:set var="tableHeadingWidth" value="400%"/>
    </c:otherwise>
</c:choose>
<div class="compareWrap">
    <h1>${properties.generalHeading}</h1>
    <div class="tableScrollWrapper">
        <div class="compare">
            <c:choose>
                <c:when test="${not empty compareListProducts}">
                    <div class="table" cellspacing="0" cellpadding="0">
                        <div class="tableBody">
                            <div class="tableRow">
                                <div class="tableData" style="width: ${tableDataWidth}">&nbsp;</div>
                                <c:forEach items="${compareListProducts}" var="product">
                                    <div class="tableData" style="width: ${tableDataWidth}">
                                        <a href="${product.pagePath}">
                                            <div class="compareName">
                                                    ${product.title}
                                            </div>
                                            <div class="compareImage">
                                                <img src="${product.image.src}">
                                            </div>
                                        </a>
                                    </div>
                                </c:forEach>
                            </div>
                            <div class="tableRow">
                                <div class="tableData" style="width: ${tableDataWidth}"><b>Price</b></div>
                                <c:forEach items="${compareListProducts}" var="product">
                                    <c:set var="price"
                                           value="<%=((Product)pageContext.getAttribute("product")).getPIMProduct().getProperty("./price", String.class)%>"/>
                                    <div class="tableData" style="width: ${tableDataWidth}">
                                        <div class="price">Rs. ${(price eq ""  || empty price)? "NA" : price}</div>
                                    </div>
                                </c:forEach>
                            </div>
                            <div class="tableRow">
                                <div class="tableData" style="width: ${tableDataWidth}"><b>Warranty</b></div>
                                <c:forEach items="<%=productCompare.getWarranty()%>" var="warranty">
                                    <div class="tableData"
                                         style="width: ${tableDataWidth}">${warranty eq "null" ? "NA" : warranty} </div>
                                </c:forEach>
                            </div>

                            <c:forEach items="${properties.compareTag}" var="item">
                                <div class="">
                                    <c:set var="tagName"
                                           value="<%=productCompare.resolveTagName(String.valueOf(pageContext.getAttribute("item")))%>"/>
                                    <c:set var="childTagList"
                                           value="<%=productCompare.resolveTagChildren(String.valueOf(pageContext.getAttribute("item")))%>"/>
                                    <c:if test="${not empty childTagList}">
                                        <div class="tableHeading" style="width: ${tableHeadingWidth}" >${tagName}</div>
                                    </c:if>
                                </div>

                                <c:forEach items="${childTagList}" var="childTagName">
                                    <div class="tableRow">
                                        <div class="tableData" style="width: ${tableDataWidth}"><b>${childTagName}</b>
                                        </div>
                                        <c:forEach items="${techSpecMapList}" var="productMap">
                                            <c:set var="flag" value="true"/>
                                            <c:forEach items="${productMap}" var="techSpecMap">
                                                <c:if test="${techSpecMap.key eq childTagName}">
                                                    <div class="tableData" style="width: ${tableDataWidth}">
                                                        ${techSpecMap.value}
                                                    </div>
                                                    <c:set var="flag" value="false"/>
                                                </c:if>
                                            </c:forEach>
                                            <c:if test="${flag}">
                                                <div class="tableData" style="width: ${tableDataWidth}"></div>
                                            </c:if>
                                        </c:forEach>
                                    </div>
                                </c:forEach>
                            </c:forEach>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <b>No Data found </b>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<%
    } catch (Exception ex) {
        ex.printStackTrace(System.out);
    }
%>
<div style="clear:both"></div>