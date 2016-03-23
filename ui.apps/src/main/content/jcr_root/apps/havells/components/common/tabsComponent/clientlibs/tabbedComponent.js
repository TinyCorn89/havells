;(function($){
    "use strict";

    $.fn.toggleComponentTabs = function(opts) {
        return this.each(function() {

            var $item = $(this);

            //Tab to accordion script
            $item.find('#parentHorizontalTab').easyResponsiveTabs({
                type: 'default',
                width: 'auto',
                fit: true,
                tabidentify: 'hor_1',
                activate: function(event) {
                    var $tab = $(this);
                    var $info = $('#nested-tabInfo');
                    var $name = $('span', $info);
                    $name.text($tab.text());
                    $info.show();
                }
            });
        });
    }

    $.fn.toggleAndHideParsys = function(opts){
        return this.each(function(){
            var $item = $(this);
            var noOfParsys = opts.noOfParsys;

            hideAllTabContent();
            $item.find("#content0").show();
            hideOrShowParsys("tabContent0","show");
            $item.find("#tabber0").addClass("resp-tab-active");

            $item.find("li").on('click', function() {
                var li = this;
                var id = li.id;
                var counter = id.substr(6,id.length);
                toggleTab(counter);
                return false;
            });

            function toggleTab(obj){
                hideAllTabContent();
                $item.find('.horizontalTabs').removeClass("resp-tab-active");
                $item.find("#content"+obj).show();
                hideOrShowParsys("tabContent"+obj,"show");
                $item.find("#tabber"+obj).addClass("resp-tab-active");
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
                $item.find('.genericTabContent').hide();
                for (var i = 0; i < noOfParsys; i++) {
                    hideOrShowParsys("tabContent"+i,"hide");
                }
            }
        });
    }
})(jQuery);
