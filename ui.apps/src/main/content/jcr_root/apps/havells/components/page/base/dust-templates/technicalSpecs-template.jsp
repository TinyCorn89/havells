<script type="text/html" id="techSpecsTemplate">
    <div class="techSpecWrapper">
        <div class="techSpecWrapperTitle">{key}</div>
          {#features}
                <div class="techSpecContentWrapper">
                    <div class="width20Pre">{heading}</div>
                    <div class="borderRightNone">{value}</div>
                </div>
          {/features}
</script>
<script type="text/html" id="techSpecsImagesTemplate">
    {#techSpecsImages}
        <div class="specDrawing">
                <a href="{imagePath}" class="specFancybox"><img src="{imagePath}">
                <div class="viewImage">VIEW IMAGE</div>
        </a>
        </div>
    {/techSpecsImages}
</script>`
