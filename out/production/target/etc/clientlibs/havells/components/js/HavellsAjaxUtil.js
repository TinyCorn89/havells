;
var CommonUtil = CommonUtil || {
    getJsonData: function (url, dataType, callback) {
        jQuery.ajax({
            type: "GET",
            url: url,
            dataType: dataType,
            success: function (response) {
                callback(response);
            },
            error: function (jqXHR, textStatus, e) {
                console.log("error in getting json from youtube" + e);
                return;
            }
        });
    },
    loadTemplate: function (dataJson, template) {
        var content = "";
        try {
            dust.loadSource(dust.compile(template, 'info'));
            dust.render('info', dataJson, function (err, out) {
                if (out !== undefined) {
                    content = out;
                }
            });
        } catch (ex) {
        }
        return content;
    }
};
