<script type="text/html" id="detailFeatureTemplate">
    <div class="havellsBringWrapper">
            {#detailsFeatures}
                <div class="havellsBringInner">
                    <div class="havellsBringLeft" id="havellsBringLeft{$idx}">
                        <img src="{imagePath}"/>
                    </div>
                    <div class="havellsBringRight">
                        <h6>{heading}</h6>
                        <p>{description}</p>
                    </div>
                </div>
            {/detailsFeatures}
    </div>
 </script>
