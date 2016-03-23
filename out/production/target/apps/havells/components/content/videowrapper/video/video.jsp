<%@include file="/apps/havells/global.jsp"%>
<c:set var ="videourl" value ="http://www.youtube.com/embed/${properties.videoID}" />
<c:set var ="videothumbnail" value ="http://img.youtube.com/vi/${properties.videoID}/mqdefault.jpg" />
<input type="hidden" name="videoId" value="${properties.videoID}" />
<c:set var ="width" value ="${properties.width}"/>
<c:set var ="height" value ="${properties.height}"/>
<c:if test="${empty width}" >
    <c:set var="width" value="560" />
</c:if>
<c:if test="${empty height}" >
    <c:set var="height" value="315" />
</c:if>
<div class="videoSliderInner" style="pxfloat: none;" width="${width}" height="${height}" >
    <c:choose>
        <c:when test="${properties.viewType == 'fancybox'}">
            <a class="mediaVideo" href="${videourl}">
                <img src="${videothumbnail}" width="${width}" height="${height}"><em></em>
                <img src="/etc/clientlibs/havells/image/video-play-icon.png" class="play-icon"></a>

        </c:when>
        <c:otherwise>
            <iframe width="${width}" height="${height}" src="${videourl}" frameborder="0" allowfullscreen>
            </iframe>
        </c:otherwise>
    </c:choose>
    <div class="videoName">${properties.videolabel}</div>
    <div class="viewWrapper">
    </div>
</div>
