package com.havells.util;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.havells.core.model.product.ProductConstant;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;


public class RenditionUtil {


    /**
     * @param path
     * @param resourceResolver
     * @param imageRendition
     * @return
     */
    public static String getRendition(String path, ResourceResolver resourceResolver, String imageRendition) {

        try {
            if (!StringUtils.isEmpty(path)) {
                Resource resource = resourceResolver.getResource(path);
                if (resource != null) {
                    Asset asset = resource.adaptTo(Asset.class);
                    Rendition rendition = asset.getRendition(imageRendition);
                    if (rendition != null) {
                        return rendition.getPath();
                    }
                }
            }
        } catch (Exception ex) {
        }
        return ProductConstant.DEFAULT_IMAGE_PATH;

    }

    public static String getDefaultOriginalImage(String path, ResourceResolver resourceResolver, String imageRendition) {
        String renditionImagePath = getRendition(path, resourceResolver, imageRendition);
        return  renditionImagePath.equals(ProductConstant.DEFAULT_IMAGE_PATH) ? path : renditionImagePath;
    }
}

