<%@include file="/apps/havells/global.jsp" %>

<a target="_blank" href="${properties.path != null ? properties.path : "{href}" }">
    <div class="pdfImageIcon">

        <img src="${ (properties.image ne null) ? properties.image : '/etc/clientlibs/havells/image/pdf-icon.png'}" height="46px" width="47px">

    </div>
    <div class="pdfDownloadSection">
        ${properties.name != null ? properties.name : "{title}"}
        <span>Download PDF</span>
    </div>
</a>
