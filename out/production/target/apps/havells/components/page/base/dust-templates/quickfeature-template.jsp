<script type="text/html" id="quickFeatureTemplate">
        <ul>
            {#quickFeature}
            <li>
                <h4>{heading}</h4>
                <p>{description}</p>
            </li>
            {/quickFeature}
        </ul>
        {?price}
        <div class="mrpWrapper">
            <div id="onlyMrp"> MRP: Rs. {price} /-</div>
        </div>
        {/price}
</script>
<script type="text/html" id="colorWrapperTemplate">
    {?colorVariants} <span>Color Options</span> {/colorVariants}
    {#colorVariants}
    <div class="colorDiv" index="{$idx}" id="{colorVariantPath}" data-iscolor="color" onclick="setAllVariantsData($(this))" >
            <a href='javascript:;'>
            <img src="{colorImg}" title="{color}" alt="{color}">
            </a>
        </div>
    {/colorVariants}
</script>
