//FancyBox initialized when using modalGeneric class
jQuery(document).ready(function () {
    var image = $('.havellsImage .imgAnchor');
    if(image != undefined){
        image.fancybox({
            maxWidth: 800,
            maxHeight: 600,
            fitToView: false,
            width: '60%',
            height: '60%',
            autoSize: false,
            closeClick: false,
            openEffect: 'none',
            closeEffect: 'none',
            type: 'iframe'
        });
    }
});
