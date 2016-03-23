<%@include file="/apps/havells/global.jsp"%>
<ul>
     <c:if test="${editMode}">
                 Drop category details here.
     </c:if>
      <cq:include path="categoryPar" resourceType="foundation/components/parsys"/>
     <c:if test="${editMode}">
                    Drop category details here.
     </c:if>
</ul>

