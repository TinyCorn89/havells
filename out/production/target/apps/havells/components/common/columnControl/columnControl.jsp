<%@include file="/apps/havells/global.jsp" %><%
%><%@ page import="com.day.cq.wcm.api.WCMMode" %>

<c:if test="${properties.columnLayout ne null}">
    <c:set var="colCount" value="${properties.columnLayout-1}"/>
</c:if>

<c:set var="layout" value="${properties.columnLayout eq \"2\" ? properties.twoColumnLayout : properties.threeColumnLayout }"/>

<div class="row cq-colctrl-lt0 vLeft${properties.leftBorder} vRight${properties.rightBorder}
hTop${properties.topBorder} hBottom${properties.bottomBorder}">
    <c:if test="${properties.columnLayout ne null}">
        <c:forEach begin="0" end="${colCount}" var="item" varStatus="loop">
            <div class="parsys_column cq-colctrl-lt${colCount-1}${layout}-c${loop.index}
            vMiddle${ !loop.last ? properties.middleBorder : ''} vMiddleDiscrete${properties.middleDiscreteBorder}"
                    style="padding: ${properties.padding}px;">
                <cq:include path="par${loop.index}" resourceType="foundation/components/parsys" />
            </div>
        </c:forEach>
    </c:if>
</div>
<div style="clear:both;"></div>
