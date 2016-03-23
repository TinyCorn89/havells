<%@include file="/apps/havells/global.jsp"%>

<c:set var ="border" value ="${(properties.includeBorder) ? 'row-border' :''}" />

<li class="row-icn ${properties.cssicon} ${border}" style="background-image: url(${properties.fileReference});">
    <a href="javascript:;">
        <strong>${properties.title}</strong>
        ${properties.description}
    </a>
</li>

