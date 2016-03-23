<c:set var="currentContentPage" value="${annualReports.currentContentPage}"/>
<div class="paginationAwards">
    <ul>
        <c:forEach begin="1" end="${annualReports.lastContentPage}" var="i">
            <c:choose>
                <c:when test="${currentContentPage eq i}">
                    <li>
                        <a class="active">${i}</a> |
                    </li>
                </c:when>
                <c:otherwise>
                    <li>
                        <a href="#" data-path="${annualReports.dataPath}${i}.html"
                           class="link-${i} pageLink">
                                ${i}
                        </a> |
                    </li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <c:if test="${annualReports.currentContentPage lt annualReports.lastContentPage}">
            <li>
                <a href="#" data-path="${annualReports.dataPath}${currentContentPage + 1}.html"
                   class="next pageLink" >
                    Next
                </a>
            </li>
        </c:if>
    </ul>
</div>

<script>
    $('.pageLink').on('click', function (e) {
        e.preventDefault();
        callContent($(this).attr("data-path"));
    });
</script>

