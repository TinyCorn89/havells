var googleMapUtil = googleMapUtil || {};
googleMapUtil.dealerLocator = {
    markers : [],
    dealer: 0,
    map: null,
    setDealer: function (dealer) {
        this.dealer = dealer;
    },
    getMapMarker: function () {
        for (var i = 0; i < this.dealer.length; i++) {

            this.codeAddress(this.dealer[i].address, this.dealer[i].dealername);

        }
    },
    initialize: function () {
        console.log("map initialized");
        var latitude = 28.6544346;
        var longitude = 77.16888;
        var mapProp = {
            center: new google.maps.LatLng(latitude, longitude),
            zoom: 10,
            mapTypeId: google.maps.MapTypeId.ROADMAP

        };
        map = new google.maps.Map(document.getElementById("googleMap"), mapProp);
    },
    codeAddress: function (address, dealerName) {
        var geocoder = new google.maps.Geocoder();
        geocoder.geocode({'address': address}, function (results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                map.setCenter(results[0].geometry.location);
                var contentString = dealerName;
                var infowindow = new google.maps.InfoWindow({
                    content: contentString
                });
                var marker = new google.maps.Marker({
                    map: map,
                    position: results[0].geometry.location
                });
                googleMapUtil.dealerLocator.addMarker(marker);
                google.maps.event.addListener(marker, 'click', function () {
                    infowindow.open(map, marker);
                });
            } else if (status === google.maps.GeocoderStatus.OVER_QUERY_LIMIT) {
                setTimeout(function () {
                    googleMapUtil.dealerLocator.codeAddress(address, dealerName);
                }, 1);
            } else {

            }

        });
    },
    addMarker: function (marker) {
        googleMapUtil.dealerLocator.markers.push(marker);
    },
    removeMarker: function () {
        for (var i = 0; i < googleMapUtil.dealerLocator.markers.length; i++) {
            googleMapUtil.dealerLocator.markers[i].setMap(null);
        }
        googleMapUtil.dealerLocator.markers = [];
    }
}
if ($("div#googleMap").length !== 0) {
    googleMapUtil.dealerLocator.initialize();
}

