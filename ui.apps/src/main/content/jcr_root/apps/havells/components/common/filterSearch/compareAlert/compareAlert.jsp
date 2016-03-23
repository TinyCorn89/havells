<%@ page import="com.havells.commons.sling.Resources" %>
<%@include file="/apps/havells/global.jsp"%>

<c:set var="oneProduct" value='${not empty properties.oneProduct ? properties.oneProduct : "Select atleast Two products" }'/>
<c:set var="existsProduct" value='${not empty properties.existsProduct ? properties.existsProduct : "Product Already added in the list"}'/>
<c:set var="threeProducts" value='${not empty properties.threeProducts ? properties.threeProducts : "Can not allow to add more than 3 products"}'/>

<c:set var="comparePagePath" value="${properties.comparePath}"/>
<c:choose>
    <c:when test="${not empty properties.comparePath}">
        <c:choose>
            <c:when test="<%=Resources.isInternalURL(String.valueOf( pageContext.getAttribute("comparePagePath") ),resourceResolver)%>">
                <c:set var="comparePath" value="${comparePagePath}.html"/>
            </c:when>
            <c:otherwise>
                <c:set var="comparePath" value="${comparePagePath}"/>
            </c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
        <c:set var="comparePath" value="/content/havells/en/comparePage.html"/>
    </c:otherwise>
</c:choose>
<div class="errorMessage"></div>
<div class="compareWrapperOuter">
    <div class="compareWrapper">
        <div class="compareText">compare the selected products</div>
        <div class="compareCross"><a href="javascript:;">X</a></div>
    </div>
    <div class="mainCompare" style="display: block">
        <div class="tableDiv">
            <span class="productInfo"></span>

            <div class="tableCellDiv">
                <div class="imageSmallCompare addAnotherProduct">
                    <i class="fa fa-plus"></i></div>
                <div class="compareDetailDiv">
                    <h6>Add another <br>
                        item</h6>
                </div>
            </div>
        </div>
        <div class="compareBtnDiv">
            <input type="hidden" id="comparePageRedirect" value="${comparePath}"/>
            <input type="hidden" id="warningMessage" value=""/>
            <a  href="#" target="_blank"><span>Compare</span></a>
        </div>
    </div>
</div>
<script type="text/javascript">

    jQuery(document).ready(function () {
        Havells.CompareProductObj.compareWrapperClick();
        Havells.CompareProductObj.setRangeTag( '${properties.tag ne null and not empty properties.tag ? properties.tag[0] : " "}' );
        Havells.CompareProductObj.setWarningMessages("${oneProduct}" , "${existsProduct}" , "${threeProducts}");
    });
</script>