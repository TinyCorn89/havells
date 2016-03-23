package com.havells.servlet.pim;

import com.adobe.cq.commerce.api.CommerceException;
import com.adobe.cq.commerce.api.Product;
import com.havells.commons.sling.Resources;
import com.havells.core.model.VariantsUtil;
import com.havells.core.model.product.HeadingDescModel;
import com.havells.core.model.product.ProductConstant;
import com.havells.core.model.product.ProductDetails;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.util.*;

@Component(immediate = true, metatype = false, label = "VariationInformation")
@Service
@Properties(value = {
        @Property(name = "sling.servlet.methods", value = "GET"),
        @Property(name = "sling.servlet.paths", value = "/bin/variantInfo.json")
})
public class ProductInfoServlet extends SlingSafeMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(ProductInfoServlet.class);
    public static final String IMAGE_PATH = "imagePath";

    JSONObject jsonObject = new JSONObject();
    ResourceResolver resourceResolver=null;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try {
            String path = request.getParameter("path");
            response.setCharacterEncoding("UTF-8");

            resourceResolver = request.getResourceResolver();
            Resource variantsResource = resourceResolver.getResource(path);
            ProductDetails productDetails = new ProductDetails(variantsResource);

            if (productDetails != null) {
                /** Product Info **/
                jsonObject.put("productInfo", getBasicProductInfo(productDetails));

                /*** Product images **/
                jsonObject.put("productImages", getProductImages(productDetails));

                String productImagePath = productDetails.getDefaultImage();
                jsonObject.put("productImage", productImagePath);

                /** Quick features ***/
                jsonObject.put("quickFeature", getQuickFeatures(productDetails));

                /** Quick features ***/
                jsonObject.put("accessoryFeature", getAccessoryFeatures(productDetails));

                setTechnicalSpecsInfo(productDetails, resourceResolver);

                jsonObject.put("techSpecsImages", getTechnicalSpecsImages(productDetails));

                /** Details features ***/
                jsonObject.put("detailsFeatures", getDetailsFeatures(productDetails));
                jsonObject.put("technicalDrawing", getTechnicalDrawingImages(productDetails));

                jsonObject.put("colorVariants", getColorVariants(variantsResource));
                jsonObject.put("faqs", productDetails.getFaqs());

                response.getWriter().write(jsonObject.toString());
            }
        } catch (Exception ex) {
            LOG.error("Exception while hitting servlet for getting product information",ex);
            ex.printStackTrace();
            response.setStatus(SlingHttpServletResponse.SC_EXPECTATION_FAILED);
        }
    }

    /**
     * Unique Technical specifications
     * @param productDetails
     * @param resourceResolver
     * @throws JSONException
     */
    private void setTechnicalSpecsInfo(ProductDetails productDetails, ResourceResolver resourceResolver) throws JSONException{

        JSONArray techSpecsKeys = new JSONArray();
        JSONObject techSpecsArray = new JSONObject();
        int count = 0;
        Map<String,List> technicalSpecsMap = productDetails.getTechnicalSpec();
        if(technicalSpecsMap != null && !technicalSpecsMap.isEmpty()){
            Set<Map.Entry<String, List>> entries = technicalSpecsMap.entrySet();
            if(!entries.isEmpty()){
                for (Map.Entry<String, List> techSpecs : entries) {
                    JSONArray jsonArray1 = new JSONArray();
                    ListIterator listItr = techSpecs.getValue().listIterator();
                    while (listItr.hasNext()) {
                        Map<String, String> temp = new HashMap<String, String>();
                        HeadingDescModel headingDescModel = (HeadingDescModel) listItr.next();
                        temp.put("heading", headingDescModel.getHeading());
                        temp.put("value", headingDescModel.getDescription());
                        jsonArray1.put(temp);
                    }
                    techSpecsArray.put("heading-" + count++, jsonArray1);
                    techSpecsKeys.put(techSpecs.getKey());
                }
            }
        }
        jsonObject.put("techSpecsKeys", techSpecsKeys);
        jsonObject.put("technicalSpecs", techSpecsArray);
    }
    /**
     *
     * @param productDetails
     * @return
     */
    private JSONObject getBasicProductInfo(final ProductDetails productDetails) throws  JSONException{
        JSONObject productInfo = new JSONObject();
        if(productDetails.getPrice() != 0.0) productInfo.put("price",productDetails.getAbsolutePrice() + "");
        productInfo.put("offerPrice", "0.0");
        productInfo.put(ProductConstant.PRODUCT_MANUAL_DOC_PROP, productDetails.getProductManualUrl());
        productInfo.put(ProductConstant.PRODUCT_BROCHURE_PROP, productDetails.getProductBrochureUrl());
        productInfo.put("title", productDetails.getPageTitle());

        return productInfo;
    }

    /**
     *
     * @param productDetails
     * @return
     * @throws JSONException
     */
    private JSONArray getProductImages(final ProductDetails productDetails) throws JSONException{
        String[] images = productDetails.getProductImages();
        JSONArray jsonArray = new JSONArray();
        if(ArrayUtils.isNotEmpty(images)){
            for (String image : images) {
                JSONObject json = new JSONObject();
                json.put("imagePath", image);
                jsonArray.put(json);
            }
        }
        return jsonArray;
    }

    /**
     * @param productDetails
     * @return
     */
    private JSONArray getAccessoryFeatures(final ProductDetails productDetails) {
        List<HeadingDescModel> featureList = productDetails.getAccessories();
        JSONArray featureArray = new JSONArray();
        if (featureList != null && !featureList.isEmpty()) {
            for (HeadingDescModel model : featureList) {
                Map<String, String> json = new HashMap<String, String>();
                json.put("imagePath", model.getImagePath());
                json.put("description", model.getDescription());
                json.put("heading", model.getHeading());
                featureArray.put(json);
            }
        }
        return featureArray;
    }

    /**
     *
     * @param productDetails
     * @return
     */
    private JSONArray getQuickFeatures(final ProductDetails productDetails){

        List<HeadingDescModel> featureList = productDetails.getQuickFeature();
        JSONArray featureArray = new JSONArray();
        if(featureList != null && !featureList.isEmpty()){
            for(HeadingDescModel model : featureList){
                Map<String, String> json = new HashMap<String, String>();
                json.put("description", model.getDescription());
                json.put("heading", model.getHeading());
                featureArray.put(json);
            }
        }
        return featureArray;
    }

    /**
     *
     * @param productDetails
     * @return
     */
    private JSONArray getDetailsFeatures(final ProductDetails productDetails){
        List<HeadingDescModel> dFeatureList = productDetails.getDetailFeature();
        JSONArray dFeatureArray = new JSONArray();
        if(dFeatureList != null && !dFeatureList.isEmpty()){
            for(HeadingDescModel model : dFeatureList){
                Map<String, String> json = new HashMap<String, String>();
                json.put("imagePath" , model.getImagePath());
                json.put("description", model.getDescription());
                json.put("heading", model.getHeading());
                dFeatureArray.put(json);
            }
        }
        return dFeatureArray;
    }

    /**
     *  Technical Specification images****
     * @param productDetails
     * @return
     * @throws JSONException
     */
    private JSONArray getTechnicalSpecsImages(final ProductDetails productDetails) throws JSONException, RepositoryException {
        String[] technicalSpecImages = productDetails.getTechnicalSpecImages();
        JSONArray techImagesArray = new JSONArray();
        if(technicalSpecImages != null) {
            for (String image : technicalSpecImages) {
                LOG.info("tehc specs image is " + image);
                JSONObject json = new JSONObject();
                json.put("imagePath", image);
                techImagesArray.put(json);
            }
        }
        return techImagesArray;
    }

    /**
     *  Technical Specification images****
     * @param productDetails
     * @return
     * @throws JSONException
     */
    private JSONArray getTechnicalDrawingImages(final ProductDetails productDetails) throws JSONException,RepositoryException{
        String[] technicalDrawings = productDetails.getTechnicalDrawings();
        JSONArray techImagesArray = new JSONArray();
        if(technicalDrawings != null) {
            for (String image : technicalDrawings) {
                JSONObject json = new JSONObject();
                json.put("imagePath", image);
                techImagesArray.put(json);
            }
        }
        return techImagesArray;
    }

    private JSONArray getColorVariants(final Resource variantsResource) throws JSONException, CommerceException {
        JSONArray jsonArray = new JSONArray();


        Product product = variantsResource.adaptTo(Product.class);
        LOG.info("PATH OF THE PRODUCT IN getColorVariants :"+product.getPath() + " title >>>"+product.getTitle());
        VariantsUtil variantsUtil = new VariantsUtil(product);
        variantsUtil.checkAndSetLists();
        List<Product> colorVariationList = variantsUtil.getColorVariantList();
        for (Product prod : colorVariationList){
            JSONObject variantJson = new JSONObject();
            LOG.info("path of the children of the above product :"+prod.getPath() + " title >>>"+prod.getTitle());

            String colorImg = prod.getProperty("colorImg", String.class);
            Resource resource = resourceResolver.getResource(colorImg);
            if(StringUtils.isNotEmpty(colorImg)){
                if(resource == null){
                    resource = resourceResolver.getResource(colorImg.indexOf("color") > 0 ? colorImg.replace("/color.", "/colour.").intern() : colorImg.replace("/colour.", "/color.").intern());
                    if (resource != null) variantJson.put("colorImg" , resource.getPath());
                }else{
                    variantJson.put("colorImg" , colorImg);
                }
            }
            variantJson.put("color",prod.getProperty("color", String.class));
            variantJson.put("colorVariantPath",prod.getPath());

            jsonArray.put(variantJson);
        }

        return  jsonArray;
    }
}
