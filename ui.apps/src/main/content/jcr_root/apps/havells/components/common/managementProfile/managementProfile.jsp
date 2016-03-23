<%@include file="/apps/havells/global.jsp"%>
<c:set var ="noOfTabs" value ="${properties.noOfTabs}" />
<div class="twoColRight">
    ${properties.title}
    <div class="managementWrapper">
        <div id="currentNodePath" class="${currentNode.path}"></div>
        <div class="managementProfile">
            <ul>
                <c:choose>
                    <c:when test="${not empty noOfTabs}">
                        <c:forEach begin="1" end="${noOfTabs}" var="counter">
                            <li>
                                <a id="tabber${counter}" class="horizontalTabs" href="javascript:;">
                                    <cq:include path="tab${counter}" resourceType="havells/components/common/managementProfile/ImageTextTab" />
                                </a>
                            </li>
                        </c:forEach>
                    </c:when>
                </c:choose>
            </ul>
        </div>
        <c:choose>
            <c:when test="${not empty noOfTabs}">
                <c:forEach begin="1" end="${noOfTabs}" var="counter">
                    <div id="content${counter}" class="managementContent">
                        <cq:include path="tabContent${counter}" resourceType="foundation/components/parsys" />
                    </div>
                </c:forEach>
            </c:when>
        </c:choose>
    </div>
</div>
<script>
    jQuery(document).ready(function() {
        jQuery('.managementWrapper').toggleTabs({"noOfParsys" : "${noOfTabs}"});
    });
</script>


