
function callContent(path){
    $.get(path, function (pageData) {
        if(pageData != "Fail"){

            $('#contentListId').attr("style","display:show");
            $('#noContentId').attr("style","display:none");
            $("#contentListId").html(pageData);

        }else{
            $('#noContentId').attr("style","display:show");
            $('#contentListId').attr("style","display:none");

        }

    });
}
