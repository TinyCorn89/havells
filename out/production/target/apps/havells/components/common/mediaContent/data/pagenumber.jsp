<c:set var="currentContentPage" value="${mediaContentWrapper.currentContentPage}"/>

<div class="paginationWrapper">
    <ul id="Pagination">

        <c:if test="${currentContentPage gt 1}">
            <li>
                <a href="#" data-path="${mediaContentWrapper.dataPath}${currentContentPage - 1}.html"
                   class="prev pageLink">
                    Previous
                </a>
            </li>
        </c:if>

        <c:forEach begin="1" end="${mediaContentWrapper.lastContentPage}" var="i">
            <c:choose>
                <c:when test="${currentContentPage eq i}">
                    <li>
                        <a class="active">${i}</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li>
                        <a href="#" data-path="${mediaContentWrapper.dataPath}${i}.html"
                           class="link-${i} pageLink">
                                ${i}
                        </a>
                    </li>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <c:if test="${mediaContentWrapper.currentContentPage lt mediaContentWrapper.lastContentPage}">
            <li>
                <a href="#" data-path="${mediaContentWrapper.dataPath}${currentContentPage + 1}.html"
                   class="next pageLink">
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
