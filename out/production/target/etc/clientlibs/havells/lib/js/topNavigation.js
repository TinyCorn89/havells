;(function ($) {
    "use strict";
    $.fn.topNavigation = function () {
        return this.each(function () {
            var winW = $(window).width();
            var glbFlgMblTrg = 0;
            $(".mobileTrigger").on('touchstart', function () {
                if (glbFlgMblTrg == 0) {
                    TweenMax.to($(this).find('li.frst'), 0.3, {rotation: 45, y: 7});
                    TweenMax.to($(this).find('li.scnd'), 0.3, {opacity: 0});
                    TweenMax.to($(this).find('li.thrd'), 0.3, {rotation: -45, y: -7});
                    $("header .navWrapper nav").slideDown();
                    glbFlgMblTrg = 1
                }
                else {
                    TweenMax.to($(this).find('li.frst'), 0.3, {rotation: 0, y: 0});
                    TweenMax.to($(this).find('li.scnd'), 0.3, {opacity: 1});
                    TweenMax.to($(this).find('li.thrd'), 0.3, {rotation: 0, y: 0});
                    $("header .navWrapper nav").slideUp();
                    glbFlgMblTrg = 0
                }
            });

            //Add function in mobile navigation
            if (winW < 768) {
                $("header .navWrapper .topRight nav ul li a").on("touchstart", function (e) {
                    //e.preventDefault();
                    $(this).parent().find(".subNavWrapper").slideToggle();
                    $(this).parent().toggleClass("active");
                });
                $("header .navWrapper .topRight nav ul li a").on("touchstart", function (e) {
                    e.preventDefault();
                });
                $("header .navWrapper .topRight nav ul li .subNavWrapper ul li a").on("touchstart", function () {
                    $(this).parent().find("ul:eq(0)").slideToggle();
                    if ($(this).parent().find("ul").css("display") == "none" || $(this).parent().find("ul").css("display") == undefined) {
                        window.location.href = $(this).attr("href");
                    }
                });
            }
            if (winW < 599) {
                $(document).on("touchstart", "header .topNav .topNavWrap ul li a", function () {
                    $(this).parent().toggleClass("active");
                    $(this).parent().find("ul").slideToggle();
                });
            }
            //remove + from li having no childs
            $(".subNavWrapper li").each(function () {
                $('.subNavWrapper li:has(> img)').parent().addClass("hideImageUl");
                $('.subNavWrapper li:has(> ul > li > img)').addClass("hasNoSubLinks");
                $('.subNavWrapper li:not(:has(> ul))').addClass("hasNoSubLinks");
            });
        });
    }
})(jQuery);