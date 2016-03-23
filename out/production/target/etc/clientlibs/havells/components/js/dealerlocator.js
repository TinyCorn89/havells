;
(function ($) {
    $.fn.dealerLocator = function (contentPath) {
        return this.each(function () {
            addStates(contentPath);
            var item = $(this);
            item.find('.state').change(function () {
                var state = $(this).find(":selected").val();
                addCities(state, contentPath);
            });

            item.find('.locateDealer').click(function () {
                var state = item.find('.state').find(":selected").text();
                var city = item.find('.city').find(":selected").text();
                locateDealer(contentPath, state, city);
            });

            function addStates(contentPath) {
                $.getJSON("/bin/havells/dealerlocator.states.html?contentPath=" + contentPath, function (states) {
                    var html = "<option value=''>Select State</option>";
                    var sortedStates = sortDataAlphabetically(states);
                    for (var i = 0; i < sortedStates.length; i++) {
                        html = html + "<option value='" + sortedStates[i].name + "'>" + sortedStates[i].niceName + "</option>";
                    }
                    $('.state').html(html);
                });
            }

            function addCities(state, contentPath) {
                if (state == '') {
                    $('.city').html("<option value=''>Select City</option>");
                } else {
                    $.getJSON("/bin/havells/dealerlocator.city.html?contentPath=" + contentPath + "&state=" + state, function (cities) {
                        var html = "<option value=''>Select City</option>";
                        var sortedCities = sortDataAlphabetically(cities);
                        for (var i = 0; i < sortedCities.length; i++) {
                            html = html + "<option value='" + sortedCities[i].name + "'>" + sortedCities[i].niceName + "</option>";
                        }
                        $('.city').html(html);
                    });
                }
            }

            function locateDealer(contentPath, state, city) {
                if ((state != '')) {
                    var data = {};
                    data.state = state;
                    data.contentPath = contentPath;
                    if (city != '' && city != 'Select City') {
                        data.city = city;
                    }
                    $.ajax({
                        url: '/bin/havells/dealerlocator.dealer.html',
                        dataType: 'json',
                        data: data,
                        success: function (dealers) {
                            console.log(dealers);
                            if ($("div#googleMap").length !== 0) {
                            googleMapUtil.dealerLocator.setDealer(dealers);
                            googleMapUtil.dealerLocator.removeMarker();
                            googleMapUtil.dealerLocator.getMapMarker();}
                            var tableBody = "";
                            for (var i = 0; i < dealers.length; i++) {
                                tableBody = tableBody +
                                "<tr><td>" + (i + 1) + "</td><td>" + dealers[i].dealername + "</td><td>" + dealers[i].address + "</td><td>" +
                                dealers[i].contact + "</td><td>" + dealers[i].email + "</td></tr>";
                            }
                            $('.dealerLocatorForm .tableWrapper').removeClass('hideDealer');
                            $('.dealerTable tbody').html(tableBody);
                        }
                    });
                } else {
                    alert("Please select state");
                }
            }

            function sortDataAlphabetically(jsonArray) {
                return jsonArray.sort(function (a, b) {
                    return a.niceName.localeCompare(b.niceName)
                });
            }
        });
    }
})(jQuery);