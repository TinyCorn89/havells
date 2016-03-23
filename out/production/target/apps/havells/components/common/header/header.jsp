<%@include file="/apps/havells/global.jsp" %>
<%@ page import="com.day.cq.wcm.api.components.IncludeOptions" %>

<div class="topNav">
    <div class="container">
        <div class="master">
            <div class="topNavWrap">
                <div class="mobTxtWrap">Corporate Links</div>
                <cq:include path="topLinks" resourceType="havells/components/common/topLinks"/>
            </div>
        </div>
    </div>
</div>

<div class="container">
    <div class="master">
        <div class="navWrapper">
            <cq:include path="${headerPath}/hpar/header/logo" resourceType="havells/components/common/logo"/>
            <div class="topRight">
                <cq:include path="cartWrap" resourceType="havells/components/common/cart"/>
                <c:if test="${!editMode}">
                    <div class="mobileTrigger">
                        <ul> <li class="frst"></li> <li class="scnd"></li> <li class="thrd"></li> </ul>
                    </div>
                </c:if>
                <%
                    //This code will only add the surrounding DIVs for the editbars when in EDIT mode only
                    if (WCMMode.fromRequest(request) != WCMMode.EDIT && WCMMode.fromRequest(request) != WCMMode.DESIGN) {
                        IncludeOptions.getOptions(request, true).setDecorationTagName("");
                    }
                %>
                <cq:include path="megamenu" resourceType="havells/components/common/megaMenuWrapper/menuSection"/>
            </div>
        </div>
    </div>
</div>
<div style="clear:both"></div>
