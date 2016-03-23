<%@include file="/apps/havells/global.jsp" %>
<c:set var="rating" value="${properties.commentRating}"/>
<div class="testimonialsImage"><img src="${properties.fileReference}"></div>
<div class="testimonialsText">
    <p>${properties.commentDesc}</p>

    <div class="ratingWrapper">
        <div class="rating readOnlyrating" data-average="0"
             style="height: 20px; width: 115px; overflow: hidden; z-index: 1; position: relative;">
            <div class="jRatingColor" style="width: ${rating*20}%;"></div>
            <div class="jRatingAverage" style="width: 0px; top: -20px;"></div>
            <div class="jStar"
                 style="width: 115px; height: 20px; top: -40px; background: url(/etc/clientlibs/havells/image/stars.png) repeat-x;"></div>
        </div>
        <div class="testominalName"><span>By: </span>${properties.userName}</div>
        <div class="testominalName"><span>Posted: </span>${properties.commentDate}</div>
    </div>
</div>
