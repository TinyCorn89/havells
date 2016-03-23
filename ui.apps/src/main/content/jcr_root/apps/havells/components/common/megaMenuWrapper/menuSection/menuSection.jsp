<%@ page import="com.havells.core.model.Multifield,com.day.cq.wcm.api.WCMMode,
                    com.day.cq.wcm.api.components.IncludeOptions" %>
<%@include file="/apps/havells/global.jsp"%>
<c:set var="multifield" value="<%=new Multifield(resource)%>"/>
<c:set var="oLinks" value="${multifield.list}"/>
<%
    //This code will only add the surrounding DIVs for the editbars when in EDIT mode only
    if (WCMMode.fromRequest(request) != WCMMode.EDIT && WCMMode.fromRequest(request) != WCMMode.DESIGN) {
        IncludeOptions.getOptions(request, true).setDecorationTagName("");
    }
%>
<nav>
    <input type="hidden" id="maxNavItems" value="<%=properties.get("maxNavItems", "6")%>"/>
<ul class="sf-menu megaMenuNew" id="megaMenu">
    <c:choose>
        <c:when test="${oLinks != null}">
            <c:forEach var="linkObj" items="${oLinks}" varStatus="counter">
                <li>
                    <a href="${linkObj.path}" class = "">${linkObj.link}</a>
                    <cq:include path="${linkObj.encodedName}" resourceType="havells/components/common/megaMenuWrapper/megaMenu"/>
                </li>
            </c:forEach>
        </c:when>
    </c:choose>
</ul>
</nav>
<script type="text/javascript">
    $(".subNavWrapper").ready(function () {

        var max_size = $('#maxNavItems').val();

        $('.subNavWrapper .master > ul').each(function () {
            var act_size = $(this).children().length;
            var viewUrl = $(this).parents('li').find('> a').attr('href');
            if (act_size > max_size) {
                $(this).children().slice(max_size, act_size).hide();
                $(this).append("<li> <a class='viewAllLink' href=" + viewUrl + ">View All</a></li>");
            }
        });

        var aboutNav = $('.aboutNavWrap ul');
        var aboutUrl = aboutNav.parents('li').find('> a').attr('href');
        var aboutSize = aboutNav.children().length;
        if (aboutSize > max_size) {
            aboutNav.children().slice(max_size, aboutSize).hide();
            aboutNav.append("<li> <a class='viewAllLink' href=" + aboutUrl + ">View All</a></li>");
        }

        $('.viewAllLink').each(function () {
            var element = $(this);
            var parentUl = element.parent("li").parent('ul');
            var act_size = parentUl.children().length;
            if (act_size > max_size) {
                parentUl.children().slice(max_size, act_size).hide();
                element.parent("li").show();
                element.last().attr("href", parentUl.siblings('a').attr('href'));
            }

        });
    });
    $(document).ready(function(){
        jQuery('ul.sf-menu').superfish({
            delay:       550,                            // one second delay on mouseout
            speed:       'fast',                          // slow animation speed
            autoArrows:  false                            // disable generation of arrow mark-up
        });
    });
</script>
