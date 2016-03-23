;(function($){
    "use strict";

    $.fn.toggleMilestoneTabs = function(opts) {
        return this.each(function(){

            var $item = $(this);
            var noOfParsys = opts.noOfParsys;
            var isEditMode = CQ.WCM && CQ.WCM.isEditMode(true);

            if(isEditMode){
                hideAllParsys();
                hideOrShowParsys("tabContent0","show");
            }
            $item.find('#content0').removeClass("noDisplay");
            $item.find('#tabber0').addClass("active");

            $item.find(".milestoneTab ul li").click(function(){
                var getIndex = $(this).index();

                $item.find(".milestoneTab ul li a").removeClass("active");
                $(this).children().addClass("active");

                $item.find(".Wrapper100Pre .milestoneContent").hide();
                if(isEditMode){ hideAllParsys(); }
                $item.find(".Wrapper100Pre .milestoneContent:eq("+getIndex+")").show();
                if(isEditMode){ hideOrShowParsys("tabContent"+getIndex,"show"); }
            })

            function hideOrShowParsys(parsysName,event){
                var currentNodePath = $item.find('#currentNodePath').attr('class');
                var parsysComp = CQ.WCM.getEditable(currentNodePath+"/"+parsysName);
                if(parsysComp && event == "hide") {
                    parsysComp.hide();
                } else if(parsysComp &&  event == "show"){
                    parsysComp.show();
                }
            }

            function hideAllParsys(){
                for (var i = 0; i < noOfParsys; i++) {
                    hideOrShowParsys("tabContent"+i,"hide");
                }
            }
        });
    }

})(jQuery);
