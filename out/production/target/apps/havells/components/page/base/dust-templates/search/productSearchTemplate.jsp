<script type="text/html" id="productListTemplate">
    <li>
        <a id="{pageId}" href="{pagePath}">
            <div class="productFullName">{title}</div>
            <div class="detailsOfProd">
                <div class="productCaptity">{subTitle}</div>
                <div class="productMRP">{price}</div>
            </div>
            <div class="garmentProdictDiv"><img alt="${title}" src="{imageWithRendition}"></div>
            <div class="compareDiv">
                <div class="addToCart2">
                    <label>
                        <input type="checkbox" class="checkBoxClass compareClass"
                               onclick="if($(this).is(':checked'))
                               Havells.CompareProductObj.initProductInfo('add','{skuId}') ;
                               if($(this).is(':unchecked') )
                               Havells.CompareProductObj.initProductInfo('remove','{skuId}')
                               ">
                        <span>Compare</span>
                    </label>
                </div>
            </div>
        </a>
    </li>
</script>