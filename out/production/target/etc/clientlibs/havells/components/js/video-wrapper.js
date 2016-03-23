$(document).ready(function () {

    function videoViews(videoId) {
        var jsonUrl = 'http://gdata.youtube.com/feeds/api/videos/' + videoId + '?v=2&alt=json';

        //Get Views from JSON
        $.getJSON(jsonUrl, function (data) {

            var views = data.entry.yt$statistics.viewCount,
                published = data.entry.published.$t,
                jsonDate = new Date(published);
            var date = jsonDate.toDateString();
            var html='<span>'+views+' View</span>'+' '+date;
            if(views!= null){
                $('.videoSliderInner .viewWrapper').html(html);
            }

        });
    }

    var videoId = $('input[name=videoId]').val(),
        dot = $('input[name=dot]').val() ==='true',
        arrow = $('input[name=arrow]').val() ==='true';

    if(videoId != null) {
        videoViews(videoId);
    }
    var videoSlider = $('.videoSlider .slick-slider');
    if(videoSlider != undefined){
        videoSlider.slick({
            dots: dot,
            arrows: arrow,
            slidesToShow: 3,
            infinite: false,
            responsive: [
                {
                    breakpoint: 480,
                    settings: {
                        slidesToShow: 1,
                        slidesToScroll: 1
                    }
                }
            ],
            responsive: [
                {
                    breakpoint: 768,
                    settings: {
                        slidesToShow: 1,
                        slidesToScroll: 1
                    }
                }
            ]
        });
    }
    });
