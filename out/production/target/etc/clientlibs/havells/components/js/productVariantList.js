$.fn.VariantList = function(array,clickedIndex) {

	// saving value of this
	var self = this;

	// change title
	$(".product-title").html(array[clickedIndex]['jcr:title']);

	// change product broscure links
	$(".productManual ul li:nth-child(2) a").attr("href",array[clickedIndex].productBrochure).attr("download",array[clickedIndex].productBrochure);
	$(".productManual ul li:nth-child(1) a").attr("href",array[clickedIndex].productManual);

	// change in faq
	$(".featuresAccWrapper .featureContent:nth-child(8)").find(".productFeatures").html(array[clickedIndex].faq)

	// iterate over structure
	$(".productWrapper ul:first").html("");
	for(var index=0; index<4; index++){
		var heading = array[clickedIndex].otherInfo.quickFeatures.heading[index];
		var desc = array[clickedIndex].otherInfo.quickFeatures.description[index];
		if(heading === null && desc === null){
			$(this).hide();
		}else{
			(heading === null) ? heading = "" : heading = "<h4>"+heading+"</h4>";
			(desc === null) ? desc = "" : desc = "<p>"+desc+"</p>";

			$(".productWrapper ul:first").append("<li>"+heading+""+desc+"</li>")
		}
	};


	// iterate over structure
	$(".detailFeatures .product ul").html("");
	for(var index=0; index<25; index++){
		var heading = array[clickedIndex].otherInfo.detailFeatures.heading[index];
		var desc = array[clickedIndex].otherInfo.detailFeatures.description[index];

		// null Checks , hide if both null
		if(heading === null && desc === null){
			$(this).hide();
		}else{
			(heading === null) ? heading = "" : heading = "<div class=heading>"+heading+"</div>";
			(desc === null) ? desc = "" : desc = "<div class=description>"+desc+"</div>";

			$(".detailFeatures .product ul").append("<li>"+heading+""+desc+"</li>")
		}
	}

	function getLeftNavImage(path){
		var imgSrc = path + "/_jcr_content/renditions/cq5dam.thumbnail.48.48.png";
		return '<li><a href=javascript:;><img src="'+imgSrc+'"></a></li>';
	}
	function getNavImage(path){
		return '<div class="productMain"><img src="'+path+'"></div>';
	}

	// Change Images in left nav
	$(".productMainDivLeft ul").html(getLeftNavImage(array[clickedIndex].image));
	$(".productMainDiv .slick-slider").children().remove()
	$(".productMainDiv .slick-slider").removeClass('slick-initialized');
	$(".productMainDiv .slick-slider").html( getNavImage( array[clickedIndex].image) );

	// assign to left nav image for each image
	$(array[clickedIndex].productImages).each(function(index){
		$(".productMainDivLeft ul").append( getLeftNavImage(array[clickedIndex].productImages[index]) );
		$(".productMainDiv .slick-slider").append(getNavImage(array[clickedIndex].productImages[index]));
	});
	$('.productMainDiv .slick-slider').slick({
		dots: false,
		arrows:false,
		slidesToShow: 1,
		infinite: false,
		draggable:false
	});
	$('.productMain').zoom();

	$(".productMainDivLeft ul li a").click(function(){
		var tabIndex = $(this).parent().index();
		$(".productMainDiv .slick-slider").slickGoTo(tabIndex);
		$(".productMainDivLeft ul li").removeClass("active");
		$(this).parent().addClass("active");
	})

	$('.slick-slide.variantsInner[name="'+clickedIndex+'"]').hide();
	$('.slick-slide.variantsInner[name!="'+clickedIndex+'"]').show();
}

