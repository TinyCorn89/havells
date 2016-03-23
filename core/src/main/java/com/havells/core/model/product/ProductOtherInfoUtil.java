package com.havells.core.model.product;

import com.adobe.cq.commerce.api.Product;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.havells.commons.sling.Resources;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.*;


final class ProductOtherInfoUtil {

    private static final String HEADING = "heading";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE = "image";

    public static List<ProductRecommendationModal> getRecommendedProducts(Resource resource){

        List<ProductRecommendationModal> productList = new ArrayList<ProductRecommendationModal>();
        if(resource != null) {
            ValueMap properties = resource.adaptTo(ValueMap.class);
            String[] paths = (properties != null) ? properties.get(ProductConstant.PRODUCT_RECOMMEND_PROP, new String[0]) : new String[0];
            for (String path : paths) {
                Resource pPageRes = resource.getResourceResolver().getResource(path.replace(".html", "") + ProductConstant.PRODUCT_PATH);
                if (pPageRes != null) {
                    String productDataPath = pPageRes.adaptTo(ValueMap.class).get(ProductConstant.PRODUCT_DATA, "");
                    if (!productDataPath.isEmpty()) {
                        Resource productResource = resource.getResourceResolver().getResource(productDataPath);
                        if(productResource != null) {
                            Product product = productResource.adaptTo(Product.class);
                            ProductRecommendationModal pr = new ProductRecommendationModal();
                            pr.setImageSrc(product.getImage().getSrc());
                            ValueMap productProps = productResource.adaptTo(ValueMap.class);
                            pr.setPrice((productProps != null) ? productProps.get(ProductConstant.PRODUCT_PRICE_PROP, 0.0) : 0.0);
                            pr.setProductPagePath(path + ".html");
                            pr.setProductTitle(product.getTitle());
                            productList.add(pr);
                        }
                    }
                }
            }
        }
        return productList;
    }

    public static ArrayList<HeadingDescModel> getFeature(Resource resource, int maxValue, String feature) {

        ArrayList<HeadingDescModel> features = null;
        if(resource != null){
            Resource featureResource = resource.getChild(ProductConstant.OTHER_INFO + feature);
            if (featureResource != null) {
                ValueMap properties = featureResource.adaptTo(ValueMap.class);
                if (properties != null) {
                    features = new ArrayList<HeadingDescModel>();
                    for (int i = 1; i <= maxValue; ++i) {
                        String heading = properties.get(HEADING + i, "");
                        String description = properties.get(DESCRIPTION + i, "");
                        String image = properties.get(IMAGE + i, "");
                        if (heading.equals("") && description.equals("") && image.equals("")) {
                            break;
                        }
                        HeadingDescModel headingDescModel = new HeadingDescModel(heading, description,image);
                        features.add(headingDescModel);
                    }
                }
            }
        }
        return features;
    }

    public static String[] getTechnicalDrawings(Resource resource) throws RepositoryException {

        if(resource != null){
            Resource drawingResource = resource.getChild(ProductConstant.OTHER_INFO + ProductConstant.TECHNICAL_DRAWING);
            if (drawingResource != null) {
                Node drawingNode = drawingResource.adaptTo(Node.class);
                ValueMap properties = drawingResource.adaptTo(ValueMap.class);
                if(properties != null) {
                    // Final array list to add valid image paths only
                    List<String> validTechDrawImages = new ArrayList<String>();
                    if(Resources.isPropertyMultiple(drawingResource, ProductConstant.TECH_DRAWING_IMAGES_PROP)){
                        // String array to get all image paths
                        String[] imagePaths = properties.get(ProductConstant.TECH_DRAWING_IMAGES_PROP, new String[0]);
                        // Final array list to add valid image paths only
                        // Check which paths are valid
                        for(String image : imagePaths){
                            validTechDrawImages.add(image);
                        }
                    }else{
                        String imagePath = properties.get(ProductConstant.TECH_DRAWING_IMAGES_PROP, new String());
                        validTechDrawImages.add(imagePath);
                    }
                   // Return Array List as String array
                    return validTechDrawImages.toArray( new String[validTechDrawImages.size()] );
                }
            }
        }
        return null;
    }

    public static Map<String, List> getTechnicalSpec(Resource resource) {
        Map<String, List> techSpecMap = null;
        if(resource != null){
            List<HeadingDescModel> technicalSpec = null;
            Resource technicalSpecification = resource.getChild(ProductConstant.OTHER_INFO + ProductConstant.TECHNICAL_SPECIFICATION);
            ResourceResolver resourceResolver = resource.getResourceResolver();

            if (technicalSpecification != null && resourceResolver != null) {

                TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
                Iterator<Resource> specList = technicalSpecification.listChildren();
                techSpecMap = new TreeMap<String, List>();
                StringBuffer stringBuffer;
                while (specList.hasNext()) {
                    technicalSpec = new ArrayList<HeadingDescModel>();
                    Resource temp = specList.next();
                    ValueMap valueMap = temp.adaptTo(ValueMap.class);
                    Tag tag = tagManager.resolve(valueMap.get("key", ""));
                    stringBuffer = new StringBuffer(valueMap.get("value", ""));
                    if (tag != null && !("".equals(stringBuffer.toString()))) {
                        if (!"product_tech_specification".equals(tag.getParent().getName())){
                            if(techSpecMap.containsKey(tag.getParent().getTitle())) {
                                List tempList = (List) techSpecMap.get(tag.getParent().getTitle());
                                if (isValidTag(tag,stringBuffer)) {
                                    tempList.add(new HeadingDescModel(tag.getTitle(), stringBuffer.toString(), tag));
                                }
                                techSpecMap.put(tag.getParent().getTitle(), tempList);
                            } else {
                                if (isValidTag(tag,stringBuffer)) {
                                    technicalSpec.add(new HeadingDescModel(tag.getTitle(), stringBuffer.toString(), tag));
                                    techSpecMap.put(tag.getParent().getTitle(), technicalSpec);
                                }
                            }
                        }
                    }
                }
            }
        }
        return techSpecMap;
    }

    private static boolean isValidTag(Tag tag, StringBuffer stringBuffer) {
        if (!"".equals(tag.getTitle())
                && (!ProductConstant.TECHSPEC_UNWANTED_TEXT_1.equalsIgnoreCase(stringBuffer.toString()))
                && (!ProductConstant.TECHSPEC_UNWANTED_TEXT_2.equalsIgnoreCase(stringBuffer.toString()))) {
            return true;
        }
        return false;
    }


    public static String[] getTechnicalSpecImages(Resource resource) throws RepositoryException {

        List<String> technicalSpecImagesList = null;
        if (resource != null){
            technicalSpecImagesList = new ArrayList<String>();
            Resource technicalSpecification = resource.getChild(ProductConstant.OTHER_INFO + ProductConstant.TECHNICAL_SPECIFICATION);

            if(technicalSpecification != null){
                if(Resources.isPropertyMultiple(technicalSpecification, "images")){
                    String[] imagePaths = (String[]) technicalSpecification.getValueMap().get("images");
                    for(String imagePath : imagePaths){
                        technicalSpecImagesList.add(imagePath);
                    }
                }else{
                    String imagePath = (String) technicalSpecification.getValueMap().get("images");
                    if(imagePath != null) technicalSpecImagesList.add(imagePath);
                }
            }
            return technicalSpecImagesList.isEmpty() ? null : technicalSpecImagesList.toArray( new String[technicalSpecImagesList.size()]);
        }
        return null;
    }

    public String[] getProductAccessory(Resource resource){

        return null;
    }

    public String[] getProductAccessoryImages(Resource resource){

        return null;
    }
}
