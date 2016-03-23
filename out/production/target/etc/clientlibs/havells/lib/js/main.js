var winW = $(window).width();
$(function(){
    var glbFlgMblTrg = 0;
    $(".mobileTrigger").on('touchstart',function () {
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
    if(winW < 768){
        $("header .navWrapper .topRight nav  ul  li").on("click", "span", function(e){
            //e.preventDefault();
            $(this).parent().find(".subNavWrapper").slideToggle();
            $(this).parent().toggleClass("active");
        });
        $(".subNavWrapper ul li").on("click", "i", function(){
            //alert("1");
            $(this).parent().find("ul:eq(0)").slideToggle();
            $(this).parent().toggleClass("active");
            if($(this).parent().find("ul").css("display")=="none" || $(this).parent().find("ul").css("display")==undefined){
                window.location.href=$(this).attr("href");
            }
            return false;
        });
        //remove + from li having no childs
        $(".subNavWrapper li").each(function(){
            $('.subNavWrapper li:has(> img)').parent().addClass("hideImageUl");
            $('.subNavWrapper li:has(> ul > li > img)').addClass("hasNoSubLinks");
            $('.subNavWrapper li:not(:has(> ul))').addClass("hasNoSubLinks");
        });
        $('<span></span>').insertAfter("header .navWrapper .topRight nav > ul > li > a");
        $('<i></i>').insertAfter(".subNavWrapper ul li a");

        $("header .topNav ul li").each(function(){
            $('header .topNav ul li:not(:has(> ul))').addClass("hasNoSubLinks");
        });
        $('<span></span>').insertAfter("header .topNav ul > li > a");
        $('<span></span>').insertAfter("footer .col .heading > a");

        var corpNavFlag=0;
        $("header .topNav .topNavWrap .mobTxtWrap").on("touchstart",function(){
            if(corpNavFlag==0){
                $(this).addClass("active");
                $(this).parent().css({"height":"auto","overflow":"visible"});
                corpNavFlag=1;
            }
            else if(corpNavFlag==1){
                $(this).removeClass("active");
                $(this).parent().css({"height":"35px","overflow":"hidden"});
                corpNavFlag=0;
            }
        });
        //Footer accordion in mobile device
        $("footer .col .heading").on("click","span",function(){
            $(this).parent().toggleClass("active");
            $(this).parent().next(".linkWrapper").slideToggle();
        });
    }
    if(winW < 480){
        $("header .topNav .topNavWrap ul li").on("click","span",function(){
            $(this).parent().toggleClass("active");
            $(this).parent().find("ul").slideToggle();
        });
        /*$("header .topNav .topNavWrap ul li:last-child a.signIn").on("touchstart",function(){
         $(this).parent().toggleClass("active");
         $(this).parent().find("ul.noBorder").toggleClass("showBlock")
         });*/
    }

    //Quick Nav
    var quickNavFlag=0;
    $(".quickLinkWrapper").click(function(){
        if(quickNavFlag==0){
            $(this).height("auto");
            $("li.trigger").find("i").addClass("fa-minus");
            TweenMax.to($(this), 0.6, {right: 0});
            TweenMax.to($(".quickLinkWrapper ul li"), 0.6, {marginLeft: 0});
            quickNavFlag=1;
        }
        else if(quickNavFlag==1){
            $(this).height(70);
            $("li.trigger").find("i").removeClass("fa-minus");
            TweenMax.to($(this), 0.6, {right: -70});
            TweenMax.to($(".quickLinkWrapper ul li"), 0.6, {marginLeft: 150});
            quickNavFlag=0;
        }
    });
    $(document).mouseup(function (e)
    {
        var container = $(".quickLinkWrapper");
        if (!container.is(e.target) && container.has(e.target).length === 0){
            container.height(70);
            $("li.trigger").find("i").removeClass("fa-minus");
            TweenMax.to(container, 0.6, {right: -70});
            TweenMax.to($(".quickLinkWrapper ul li"), 0.6, {marginLeft: 150});
            quickNavFlag=0;
            //$("header .topNav ul li:last-child ul").removeAttr("style");
        }
    });


    //Left Nav Accordion
    $(".twoColLeft .leftNavAccr.listingSubLink ul li").on('click','span',function(){
        $(this).parent().toggleClass("active");
        $(this).parent().find("ul:eq(0)").slideToggle();
        return false;
    });
    $("<span></span>").insertAfter(".listingSubLink li a");
    //remove + from li having no childs
    $(".listingSubLink li").each(function(){
        $('.listingSubLink li:not(:has(> ul))').addClass("hasNoSubLinks");
    });

    $(".featureClicker a").click(function(){
        $(this).toggleClass("active");
        $(this).parent().next().slideToggle();
        setTimeout('$(".testimonialsDiv").getNiceScroll().resize()',500)
    })
    $(".testimonialsDiv ").niceScroll({touchbehavior:false,autohidemode:false,cursorborder:"", cursorcolor:"#7f7f7f"});
    $(".expandAcc").click(function(){
        $(this).toggleClass("active");
        $(".twoColLeft").slideToggle();
    })
    $(".milestoneYears").click(function(){
        $(this).toggleClass("active");
        $(".milestoneTab").slideToggle();
    })
    $(".mediaVideo").fancybox({
        type:'iframe',
        padding:0
    });
    $(".mediaPrint").fancybox({padding:0});
    if(winW>768){
        $(".rateProductWrapper").click(function(){
            //$('html,body').animate({ scrollTop: 0 }, "slow");
            //$("header .topNav ul li:last-child ul").show();
        });
    }
    //form validation
    $('form').each( function(){
        $(this).validate({
            highlight: function(element) {
                $(element).addClass("validation-error");
            },
            unhighlight: function(element) {
                $(element).removeClass("validation-error");
            },
            errorPlacement: function(error,element) {
                return true;
            },
            rules: {
                password: "required",
                confirmpassword: {
                    equalTo: "#password"
                }
            }
        });
    });
    //placeholder hidden onclick - Snajeev
    $(function(){
        $('.input').data('holder',$('.input').attr('placeholder'));
        $('.input').focusin(function(){
            $(this).attr('placeholder','');
        });
        $('.input').focusout(function(){
            $(this).attr('placeholder',$(this).data('holder'));
        });

    })

    //DOB
    $( ".dob" ).datepicker({dateFormat: 'dd-mm-yy', maxDate: 0, changeMonth: true, changeYear: true, yearRange: '1910:2015'});
    //SignIn Container on focus visible script
    $("header .topNav ul li ul li .signInWrapper input").focus(function(){
        $(".signInWrapper").parent().parent().addClass("focusDisplay");
    });
    $(document).mouseup(function (e)
    {
        var container = $(".signInWrapper");
        if (!container.is(e.target) && container.has(e.target).length === 0){
            $(".signInWrapper").closest("ul").removeClass("focusDisplay");
        }
    });
});



$(function($) {
    var allAccordions = $('.accoSignin div.accoContent');
    var allAccordionItems = $('.accoSignin ul .accoHead');
    $('.accoSignin > ul > li.accoHead').click(function() {
        if($(this).hasClass('open'))
        {
            $(this).removeClass('open');
            $(this).next().slideUp();
        }
        else
        {
            allAccordions.slideUp();
            allAccordionItems.removeClass('open');
            $(this).addClass('open');
            $(this).next().slideDown();
            return false;
        }
    });

    /* Hide form input values on focus - Global*/
    $('input:text, input:password, textarea').each(function(){
        var txtval = $(this).val();
        $(this).focus(function(){
            if($(this).val() == txtval){
                $(this).val('')
            }
        });
        $(this).blur(function(){
            if($(this).val() == ""){
                $(this).val(txtval);
            }
        });
    });
    //Add form on + click
    $(".plus").click(function (){
        $(".plus").hide();
        $(".formwrapperMainadd form").show();

    });
//Accordion Jk
    $(".orderHeading").click(function(){
        if ($(this).hasClass('active')==true){
            $(".orderHeading").removeClass('active');
            $(this).next().slideUp();
            return false;
        }
        $(".orderContent").slideUp();
        $(".orderHeading").removeClass('active');
        $(this).addClass('active');
        $(this).next().slideDown();
    })
    $('.pastTab ul li').click(function(){
        $('.pastTab ul li').removeClass('active');
        $(this).addClass('active');
        $('.orderContentSection').hide();
        $('.orderContentSection:eq('+$(this).index()+')').show();

    })

    //quick query fancybox
    $(".queryFormWrapperFancy").fancybox({padding:0, scrolling:'no'});



});
