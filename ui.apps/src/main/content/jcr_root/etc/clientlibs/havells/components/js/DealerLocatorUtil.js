var findDealer = {
       getDealerData: function (contentPath, currentObject) {
            currentObject.find('.state').change(function () {
                var state = $(this).find(":selected").val();
                addCities(state, contentPath);
            });

            function addCities(state, contentPath) {
                if (state == '') {
                    $('.city').html("<option value=''>Select City</option>");
                } else {
                    $.getJSON("/bin/havells/dealerlocator.city.html?contentPath=" + contentPath + "&state=" + state, function (cities) {
                        var html = "<option value=''>Select City</option>";
                        $.each(cities.map, function (key, value) {
                            if (document.location.search.length) {
                                var cityFromParameter = findDealer.getParameterByName('city');
                                if (cityFromParameter == value) {
                                    html = html + "<option value='" + key + "' selected='selected'>" + value + "</option>";
                                }
                                else {
                                    html = html + "<option value='" + key + "'>" + value + "</option>";
                                }
                            }
                            else {
                                html = html + "<option value='" + key + "'>" + value + "</option>";
                            }
                        });
                        $('.city').html(html);
                    });
                }
            }
    },
    getParameterByName: function (name) {
        name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
        var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
            results = regex.exec(location.search);
        return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    }
};
