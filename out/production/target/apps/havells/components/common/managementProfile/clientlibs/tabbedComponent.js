;(function($){
    "use strict";

    $.fn.toggleTabs = function(opts) {
        return this.each(function() {

            var $item = $(this);
            var noOfParsys = opts.noOfParsys;
            var isEditMode = CQ.WCM && CQ.WCM.isEditMode(true);

            hideAllTabContent();
            $item.find("#content1").show();
            if(isEditMode) { hideOrShowParsys("tabContent1","show"); }
            $item.find("#tabber1").addClass("active");

            $item.find("a").on('click', function() {
                var anchor = this;
                var id = anchor.id;
                var counter = id.substr(6,id.length);
                milestoneTab(counter);
                return false;
            });

            function milestoneTab(obj){
                hideAllTabContent();
                $item.find('.horizontalTabs').removeClass("active");
                $item.find("#content"+obj).show();
                if(isEditMode) {  hideOrShowParsys("tabContent"+obj,"show"); }
                $item.find("#tabber"+obj).addClass("active");
            }

            function hideOrShowParsys(parsysName,event){
                var currentNodePath = $item.find('#currentNodePath').attr('class');
                var parsysComp = CQ.WCM.getEditable(currentNodePath+"/"+parsysName);
                if(parsysComp && event == "hide") {
                    parsysComp.hide();
                } else if(parsysComp &&  event == "show"){
                    parsysComp.show();
                }
            }

            function hideAllTabContent(){
                $item.find('.managementContent').hide();
                if(isEditMode){
                    for (var i = 1; i <= noOfParsys; i++) {
                        hideOrShowParsys("tabContent"+i,"hide");
                    }
                }
            }
        });
    }
})(jQuery);
