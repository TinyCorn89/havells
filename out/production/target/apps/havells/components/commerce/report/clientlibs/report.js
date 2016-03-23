;(function($){
    "use strict";

    $.fn.renderReport = function(options) {

        var template = $("#"+options.templateId).html();

        // TODO : call render here
        try{
            dust.loadSource(dust.compile(template,'report'));
            dust.render('report', returnIndex(options.data,options.year) , function(err, out) {
                $("#"+options.resultId).html(out);
            });
        }catch(ex){
        }

    }

    $.fn.renderReportWithoutLoad = function(options){
        try{
            dust.render('report', returnIndex(options.data,options.year) , function(err, out) {
                $("#"+options.resultId).html(out);
            });
        }catch(ex){
        }
    }

    function returnIndex(data,year){
        var report = data.report;

        for(var obj in report){
            if(report[obj].year === year){
                return report[obj].quarters;
            }
        }
    }
})(jQuery);
