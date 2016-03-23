<script type="text/html" id="accessoryTemplate">
    <div class="havellsBringWrapper">
            {#accessoryFeature}
                <div class="havellsBringInner">
                    <div class="havellsBringLeft" id="havellsBringLeft{$idx}">
                        <img src="{imagePath}"/>
                    </div>
                    <div class="havellsBringRight">
                        <h6>{heading}</h6>
                        <p>{description}</p>
                    </div>
                </div>
            {/accessoryFeature}
    </div>
 </script>
