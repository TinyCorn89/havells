<%@include file="/apps/havells/global.jsp"%>
<ul>
    <c:if test="${not empty properties.cartCheck}">
        <li>
            <div class="label">Your cart</div>
            <div class="cartValue">0</div>
        </li>
    </c:if>
    <li>
        <cq:include path="${headerPath}/stockInfo" resourceType="havells/components/content/stockInfo"/>
    </li>
</ul>
