var videoIdArray = [];

var addToVideoIdArray = function(videoId){
    videoIdArray.push(videoId);
};

var ytVideoAjax =  function(oAuthKey){
        $.ajax({
            url: "https://www.googleapis.com/youtube/v3/videos?id="+videoIdArray.toString()+"&key="+oAuthKey+"&part=snippet,statistics" ,
            success: function(response){
                $(videoIdArray).each(function(index){
                var views = response.items[index].statistics.viewCount,
                    published = response.items[index].snippet.publishedAt,
                    jsonDate = new Date(published),
		    title = response.items[index].snippet.title;
                var date = timeSince(jsonDate);
                var html='<span>'+views+' View</span>'+' '+date + ' ago';
                if(views!= null){
                    $(".viewWrapper"+videoIdArray[index]).html(html);
                }
                $(".videoName"+videoIdArray[index]).text(title);
                });
            }
        });
};

function timeSince(date) {

    var seconds = Math.floor((new Date() - date) / 1000);

    var interval = Math.floor(seconds / 31536000);

    if (interval > 1) {
        return interval + " years";
    }
    interval = Math.floor(seconds / 2592000);
    if (interval > 1) {
        return interval + " months";
    }
    interval = Math.floor(seconds / 86400);
    if (interval > 1) {
        return interval + " days";
    }
    interval = Math.floor(seconds / 3600);
    if (interval > 1) {
        return interval + " hours";
    }
    interval = Math.floor(seconds / 60);
    if (interval > 1) {
        return interval + " minutes";
    }
    return Math.floor(seconds) + " seconds";
}