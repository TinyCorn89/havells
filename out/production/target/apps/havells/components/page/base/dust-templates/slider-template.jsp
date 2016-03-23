<script type="text/html" id="sliderTemplate">
    <div class="threeSlider">
        <div class="threeSliderInner">
            <a class="active" href="javascript:;">
               <img src="{productImage}"></a></div>
              {#productImages}
                <div class="threeSliderInner"><a href="javascript:;">
                    <img src="{imagePath}"></a>
                </div>
            {/productImages}
    </div>
</script>
<script type="text/html" id="zoomSliderTemplate">
    <div class="slick-slider">
        <div class="productMain mainImg">
            <img src="{productImage}">
        </div>
        {#productImages}
        <div class="productMain"><img src="{imagePath}"></div>
        {/productImages}
     </div>
</script>
