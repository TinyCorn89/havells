;
(function ($) {
    $.fn.dealerLocator = function (contentPath) {
        return this.each(function () {
            findDealer.getDealerData(contentPath, $(this));
            var state = "", city = "", products = "";
            var item = $(this);
            item.find('.locateDealer').click(function () {
                var prevState = state;
                var prevCity = city;
                var prevProduct = products;
                state = item.find('.state').find(":selected").text();
                city = item.find('.city').find(":selected").text();
                products = item.find('.products').find(":selected").text();

                if ((state !== "Select State") && (stateFromParameter != state || cityFromParameter != city || prevProduct != products) && (prevState != state || prevCity != city || prevProduct != products)) {
                    locateDealer(contentPath, state, city, products);
                }
            });

            if (document.location.search.length) {
                var stateFromParameter = findDealer.getParameterByName('state');
                var cityFromParameter = findDealer.getParameterByName('city');
                if (stateFromParameter !== '') {
                    locateDealer(contentPath, stateFromParameter, cityFromParameter);
                    $(".formParameter").trigger("change");
                }
            }

            function locateDealer(contentPath, state, city, products) {
                if ((state != '')) {
                    var data = {};
                    data.state = state;
                    data.contentPath = contentPath;
                    if (city != '' && city != 'Select City') {
                        data.city = city;
                    }
                    if (products !== "Select Product") {
                        data.products = products;
                    }
                    $.ajax({
                        url: '/bin/havells/dealerlocator.dealer.html',
                        dataType: 'json',
                        data: data,
                        success: function (dealers) {
                            if (dealers.length !== 0) {
                                if (($("div#googleMap").length !== 0)) {
                                    try {
                                        googleMapUtil.dealerLocator.setDealer(dealers);
                                        googleMapUtil.dealerLocator.removeMarker();
                                        googleMapUtil.dealerLocator.getMapMarker();
                                    } catch (e) {
                                    }
                                }
                                var tableBody = "";
                                for (var i = 0; i < dealers.length; i++) {
                                    tableBody = tableBody +
                                    "<tr><td>" + (i + 1) + "</td><td>" + dealers[i].dealername + "</td><td>" + dealers[i].address + "</td><td>" +
                                    dealers[i].contact + "</td><td>" + dealers[i].email + "</td></tr>";
                                }
                                $('.dealerLocatorForm .tableWrapper').removeClass('hideDealer');
                                $('.dealerTable tbody').html(tableBody);
                                formSubmitFlag = true;
                            }
                            else {
                                $('.dealerLocatorForm .tableWrapper').removeClass('hideDealer');
                                $('.dealerTable tbody').html("<p>No Result found</p>")
                            }
                        }
                    });
                }
            }
        });
    }
})(jQuery);

