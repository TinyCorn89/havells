$(function(){
    var techSpecsViewImage = $(".specFancybox");
    if(techSpecsViewImage != undefined) {
        $(".specFancybox").fancybox({
            padding: 0,
            scrolling: "no",
            afterShow: function () {
                document.ontouchstart = function (e) {
                    //prevent scrolling
                    e.preventDefault();
                }
            },
            afterClose: function () {
                document.ontouchstart = function (e) {
                    //default scroll behaviour
                }
            },

            helpers: {
                overlay: {
                    locked: true
                }
            }
        });
    }
});
