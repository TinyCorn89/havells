;(function($){
    "use strict";

    $.fn.socialMediaLinks = function () {
        return this.each(function () {
            var $shareSocialDiv = $(this).parent();
            var pageTitle = document.title;
            var currentPagePath = window.location.href;
            var $anchorTagTwitter = $shareSocialDiv.find('.fa-twitter ').parent();
            var $anchorTagFacebook = $shareSocialDiv.find('.fa-facebook ').parent();
            var $anchorTagEnvelope = $shareSocialDiv.find('.fa-envelope').parent();
            var $anchorTagGooglePlus   = $shareSocialDiv.find('.fa-google-plus-square').parent();

            $anchorTagTwitter.attr("href", encodeURI("https://twitter.com/share?url=" +
                "&text=" + pageTitle +
                "&url=" + currentPagePath +
                "&eid=true"));

            $anchorTagFacebook.attr("href", encodeURI("http://www.facebook.com/sharer/sharer.php?u=" +
                currentPagePath));

            $anchorTagGooglePlus.attr("href",encodeURI("https://plusone.google.com/_/+1/confirm?hl=en&url=" + currentPagePath + "&title=" + pageTitle));

            $anchorTagEnvelope.attr("href",encodeURI("mailto:?body=" + $("#emailText").val()+
                currentPagePath + "&subject=" + $("#emailSubject").val()));
        })
    };

})(jQuery);
