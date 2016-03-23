<%@include file="/apps/havells/global.jsp"%>
    <c:set var ="videourl" value ="http://www.youtube.com/embed/${properties.videoID}" />
    <c:set var ="videothumbnail" value ="http://img.youtube.com/vi/${properties.videoID}/mqdefault.jpg" />
    <c:if test="${properties.type !=null}">
        <c:choose>
            <c:when test="${properties.type != 'image'}">
                <ul>
                    <li><a href='${videourl}' class="mediaVideo" >
                        <img src="${videothumbnail}" style="width:100%;">
                        <em></em>
                        <span>
                           <c:if test="${properties.label}!=null">
                               <i class="fa fa-plus-circle"></i> ${properties.label}
                           </c:if></span>
                    </a>
                    </li>
                </ul>
            </c:when>
            <c:otherwise>
                <ul>
                    <li><a href='${(properties.largeImage!=null) ? (properties.largeImage):(properties.fileReference)}' class="mediaPrint" >
                        <img style="width:100%;" alt="this is image" src="${properties.fileReference}">
                        <span>
                            <c:if test="${properties.label}!=null">
                                <i class="fa fa-plus-circle"></i> ${properties.label}
                            </c:if>
                        </span></a></li>
                </ul>
            </c:otherwise>
        </c:choose>
    </c:if>
