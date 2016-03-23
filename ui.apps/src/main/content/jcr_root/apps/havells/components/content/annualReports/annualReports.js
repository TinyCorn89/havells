function callContent(path){
    $.get(path, function (pageData) {
        if(pageData != "Fail"){
            $('#contentListId').show();
            $('#noContentId').hide();
            $("#contentListId").html(pageData);
        }else{
            $('#noContentId').show();
            $('#contentListId').hide();
        }
    });
}
