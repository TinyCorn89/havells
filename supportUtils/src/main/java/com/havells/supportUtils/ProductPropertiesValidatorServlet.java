package com.havells.supportUtils;

import com.adobe.cq.commerce.api.CommerceConstants;
import com.adobe.cq.commerce.api.CommerceException;
import com.adobe.cq.commerce.api.Product;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.search.Query;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.TagConstants;
import com.havells.supportUtils.model.ProductPropertiesValidatorModel;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.servlet.ServletException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.System;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by shashi on 6/9/15.
 */
@SlingServlet(paths = {"/bin/productReports"},
        methods = {"GET"}, generateComponent = false)
@Component(label = "PIM Product Properties Validator", enabled = true, metatype = false, immediate = true)
public class ProductPropertiesValidatorServlet extends SlingSafeMethodsServlet{

    private static final Logger LOG = LoggerFactory.getLogger(ProductPropertiesValidatorServlet.class);
    private List<ProductPropertiesValidatorModel> invalidPropertiesProduct;

    private static String pathToExcel;
    private static final String EXCEL_NAME = "/HavellsProductReport.xlsx";

    private ResourceResolver resourceResolver;

    interface Messages{
        String IMAGE_FOUND = "IMAGE(s) PATH FOUND";
        String IMAGE_NOT_FOUND = "IMAGE(s) PATH NOT FOUND";
        String PDF_FOUND = "PDF FOUND";
        String PDF_NOT_FOUND = "PDF NOT FOUND";
        String EMPTY_PROPERTY = "NO PATH(s) ASSIGNED TO IMAGES";
        String PROPERTY_DOESNT_EXIST = "NO SUCH PROPERTY EXISTS IN PRODUCT";
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        pathToExcel = request.getParameter("directory") != null ? (request.getParameter("directory") + EXCEL_NAME).replaceAll("//","/") : EXCEL_NAME;
        resourceResolver = request.getResourceResolver();
        invalidPropertiesProduct = new ArrayList<ProductPropertiesValidatorModel>();

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("path", ScriptConstants.HAVELLS_PRODUCTS_PATH);
        parameter.put("type", JcrConstants.NT_UNSTRUCTURED);
        parameter.put("property", "sling:resourceType");
        parameter.put("property.value", ScriptConstants.HAVELLS_PRODUCTS_RESOURCE_TYPE);

        Query query = JcrQueryUtil.getQuery(resourceResolver, parameter);

        query.setHitsPerPage(0);
        SearchResult result = query.getResult();
        List<Hit> hits = result.getHits();
        if (hits.size() > 0) {
            for(Hit hit : hits) {
                try {
                    Resource productResource = hit.getResource();
                    ProductPropertiesValidatorModel model = getProductModel(productResource);

                    if( !validateProperties(model) ){
                        invalidPropertiesProduct.add(model);
                    }
                } catch (RepositoryException e) {
                    e.printStackTrace();
                } catch (CommerceException e) {
                    e.printStackTrace();
                }
            }

        }

        JSONObject productProperties = null;
        try {
            productProperties = createWorkbookAndGetJSON();
        } catch (JSONException e) {
            LOG.error("JSON Exception occurred. Message : {}" , e.getMessage());
        }
        response.getWriter().print("Product reports is created in this directory <b>"+ pathToExcel +"</b>");
    }

    private JSONObject getProductJSON(ProductPropertiesValidatorModel model) throws JSONException {
        JSONObject productJson = new JSONObject();
        productJson.put("productRange", model.getProductRange());
        productJson.put("productCategory", model.getProductCategory());
        productJson.put("productSubCategory", model.getProductSubCategory());
        productJson.put("productSKUId", model.getProductSKUId());
        productJson.put("productPath", model.getProductPath());
        productJson.put("productName", model.getProductName());

        productJson.put("productCoverImage", model.getproductCoverImage());
        productJson.put("isProductCoverImageValid", !isPropertyInvalid(model.getproductCoverImage()));

        productJson.put("productColorImage", model.getProductColorImage());
        productJson.put("isProductColorImageValid", !isPropertyInvalid(model.getProductColorImage()));

        productJson.put("productImages", Arrays.toString(model.getProductImages()));
        productJson.put("isProductImagesValid", !isPropertyInvalid(model.getProductImages()));

        productJson.put("productManual", model.getProductManual());
        productJson.put("isProductManualValid", !isPropertyInvalid(model.getProductManual()));

        productJson.put("productBrochure", model.getProductBrochure());
        productJson.put("isProductBrochureValid", !isPropertyInvalid(model.getProductBrochure()));

        productJson.put("productTechnicalDrawingImages", Arrays.toString(model.getTechnicalDrawingImages()));
        productJson.put("isProductTechnicalDrawingImagesValid", !isPropertyInvalid(model.getTechnicalDrawingImages()));

        productJson.put("productTechSpecImages", Arrays.toString(model.getTechnicalSpecImages()));
        productJson.put("isProductTechSpecImagesValid", !isPropertyInvalid(model.getTechnicalSpecImages()));

        return productJson;
    }

    private void createExcelRow(XSSFSheet spreadsheet, int rowCount, ProductPropertiesValidatorModel model) {
        XSSFRow row = spreadsheet.createRow(rowCount);

        int cellCount = 0;

        row.createCell(cellCount++).setCellValue(model.getProductRange());
        row.createCell(cellCount++).setCellValue(model.getProductCategory());
        row.createCell(cellCount++).setCellValue(model.getProductSubCategory());
        row.createCell(cellCount++).setCellValue(model.getProductSKUId());
        row.createCell(cellCount++).setCellValue(model.getProductPath());
        row.createCell(cellCount++).setCellValue(model.getProductName());

        row.createCell(cellCount++).setCellValue( model.getproductCoverImage() );
        row.createCell(cellCount++).setCellValue( isPropertyInvalid(model.getproductCoverImage()) ? Messages.IMAGE_NOT_FOUND : Messages.IMAGE_FOUND);

        row.createCell(cellCount++).setCellValue( model.getProductColorImage() );
        row.createCell(cellCount++).setCellValue( isPropertyInvalid(model.getProductColorImage()) ? Messages.IMAGE_NOT_FOUND : Messages.IMAGE_FOUND);

        row.createCell(cellCount++).setCellValue( Arrays.toString(model.getProductImages()) );
        row.createCell(cellCount++).setCellValue( getCellValue(model.getProductImages()) );

        row.createCell(cellCount++).setCellValue( Arrays.toString(model.getTechnicalDrawingImages()) );
        row.createCell(cellCount++).setCellValue( getCellValue(model.getTechnicalDrawingImages()) );

        row.createCell(cellCount++).setCellValue( Arrays.toString(model.getTechnicalSpecImages()) );
        row.createCell(cellCount++).setCellValue( getCellValue(model.getTechnicalSpecImages()) );

        row.createCell(cellCount++).setCellValue( model.getProductBrochure() );
        row.createCell(cellCount++).setCellValue( isPropertyInvalid(model.getProductBrochure()) ? Messages.PDF_NOT_FOUND : Messages.PDF_FOUND);

        row.createCell(cellCount++).setCellValue( model.getProductManual() );
        row.createCell(cellCount++).setCellValue( isPropertyInvalid(model.getProductManual()) ? Messages.PDF_NOT_FOUND : Messages.PDF_FOUND);
    }
    private String getCellValue(String[] propertyValue){
        if(propertyValue == null){
            return Messages.PROPERTY_DOESNT_EXIST;
        }else{
            if(propertyValue.length == 0){
                return Messages.EMPTY_PROPERTY;
            }else{
                if(isPropertyInvalid(propertyValue)){
                    return Messages.IMAGE_NOT_FOUND;
                }else{
                    return Messages.IMAGE_FOUND;
                }
            }
        }
    }

    private void createExcelHeaderRow(XSSFSheet spreadsheet, int rowCount) {
        XSSFRow row = spreadsheet.createRow(rowCount);

        int cellCount = 0;

        row.createCell(cellCount++).setCellValue("Product Range");
        row.createCell(cellCount++).setCellValue("Product Category");
        row.createCell(cellCount++).setCellValue("Product Sub Category");
        row.createCell(cellCount++).setCellValue("Product SKU ID");
        row.createCell(cellCount++).setCellValue("Product Path");
        row.createCell(cellCount++).setCellValue("Product Name");

        row.createCell(cellCount++).setCellValue("Product Cover Image");
        row.createCell(cellCount++).setCellValue("Status");

        row.createCell(cellCount++).setCellValue("Product Color Image");
        row.createCell(cellCount++).setCellValue("Status");

        row.createCell(cellCount++).setCellValue("Product Images");
        row.createCell(cellCount++).setCellValue("Status");

        row.createCell(cellCount++).setCellValue("Tech Drawing Images");
        row.createCell(cellCount++).setCellValue("Status");

        row.createCell(cellCount++).setCellValue("Tech Spec Images");
        row.createCell(cellCount++).setCellValue("Status");

        row.createCell(cellCount++).setCellValue("Product Brochure");
        row.createCell(cellCount++).setCellValue("Status");

        row.createCell(cellCount++).setCellValue("Product Manual");
        row.createCell(cellCount++).setCellValue("Status");
    }

    private JSONObject createWorkbookAndGetJSON() throws JSONException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("Products Info");
        int rowCount = 0;
        createExcelHeaderRow(spreadsheet , rowCount++);
        // Creates a blank Row after Header
        spreadsheet.createRow(rowCount++);

        JSONArray productsArray = new JSONArray();

        Iterator<ProductPropertiesValidatorModel> listIterator = invalidPropertiesProduct.iterator();

        while(listIterator.hasNext()){
            ProductPropertiesValidatorModel model = listIterator.next();

            JSONObject productModel = getProductJSON(model);
            productsArray.put(productModel);

            createExcelRow(spreadsheet , rowCount++ , model);
        }

        JSONObject propertiesJSON = new JSONObject();
        propertiesJSON.put("products", productsArray);
        propertiesJSON.put("size", invalidPropertiesProduct.size());


        FileOutputStream out = null;
        try {
            out = new FileOutputStream( new File(pathToExcel) );
            workbook.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            LOG.error("Exception occurred. {}" , e.getMessage());
        } catch (IOException e) {
            LOG.error("Exception occurred. {}", e.getMessage());
        }

        return propertiesJSON;
    }

    private ProductPropertiesValidatorModel getProductModel(Resource productResource) throws CommerceException {
        ValueMap propertyMap = productResource.getValueMap();
        /**
         * properties from product node
         */
        String productRange = new String();
        String productCategory = new String();
        String productSubCategory = new String();

        String productSKUId = propertyMap.get( ScriptConstants.SKU_ID , String.class );
        String productPath = productResource.getPath();
        String productTitle = propertyMap.get(JcrConstants.JCR_TITLE, String.class);
        String productManual = propertyMap.get(ScriptConstants.PRODUCT_MANUAL_DOC_PROP, String.class);
        String productBorchure = propertyMap.get(ScriptConstants.PRODUCT_BROCHURE_PROP, String.class);
        String productColorImg = propertyMap.get(ScriptConstants.COLOR_IMAGE_PROP, String.class);
        String[] productImages = propertyMap.get(ScriptConstants.PRODUCT_IMAGES_PROP, String[].class);

        String[] cqTags = propertyMap.get(TagConstants.PN_TAGS, String[].class);
        if(cqTags != null){
            String cqTag = cqTags[0];

            String[] productTagInfo = cqTag.substring( cqTag.indexOf(":")+1, cqTag.length()).split("/");
            try{
                productRange = productTagInfo[0];
                productCategory = productTagInfo[1];
                productSubCategory = productTagInfo[2];
            }catch(ArrayIndexOutOfBoundsException event){
                LOG.error("Array index out of bounds. {}" , event.getMessage());
            }

        }else if ( propertyMap.get(CommerceConstants.PN_COMMERCE_TYPE, String.class ) != null && ( propertyMap.get(CommerceConstants.PN_COMMERCE_TYPE, String.class ).equals(ScriptConstants.VARIANT) )){
            /**
             * in case of variant, if can't find tag in product, go to it's parent
             */
            Product variantProduct = productResource.adaptTo(Product.class);
            Product parentProduct = variantProduct.getBaseProduct();

            if(parentProduct != null){
                ValueMap map = parentProduct.adaptTo(Resource.class).getValueMap();
                String[] tagsArray = map.get(TagConstants.PN_TAGS, String[].class);
                if(tagsArray != null){
                    String cqTag = tagsArray[0];
                    String[] productTagInfo = cqTag.substring( cqTag.indexOf(":")+1, cqTag.length()).split("/");
                    try{
                        productRange = productTagInfo[0];
                        productCategory = productTagInfo[1];
                        productSubCategory = productTagInfo[2];
                    }catch(ArrayIndexOutOfBoundsException event){
                        LOG.error("Array index out of bounds. {}" , event.getMessage());
                    }
                }
            }
        }

        /**
         * properties from otherInfo Nodes
         */
        Resource imageRes = productResource.getChild("image");
        String coverImage = imageRes != null ? imageRes.getValueMap().get(ScriptConstants.COVER_IMAGE_PROP, String.class) : new String();

        Resource technicalSpec = productResource.getChild(ScriptConstants.OTHER_INFO + ScriptConstants.TECHNICAL_SPECIFICATION);
        String[] techSpecImages = technicalSpec != null ? technicalSpec.getValueMap().get(ScriptConstants.TECH_SPECS_IMAGES_PROP , String[].class) : new String[0];

        Resource technicalDrawing = productResource.getChild(ScriptConstants.OTHER_INFO + ScriptConstants.TECHNICAL_DRAWING);
        String[] techDrawingImages = technicalDrawing != null ? technicalDrawing.getValueMap().get(ScriptConstants.TECH_DRAWING_IMAGES_PROP , String[].class) : new String[0];

        return new ProductPropertiesValidatorModel(productRange, productCategory, productSubCategory ,productSKUId, productPath, productTitle , coverImage,
                 productColorImg, productImages, techSpecImages, techDrawingImages, productManual, productBorchure);
    }

    private boolean validateProperties(ProductPropertiesValidatorModel model){
        boolean allPropertiesValid = true;

        String productPath = model.getProductPath();
        String productCoverImage = model.getproductCoverImage();
        String[] productImages = model.getProductImages();
        String productColorImg = model.getProductColorImage();
        String[] productTechSpecImages = model.getTechnicalSpecImages();
        String[] productTechDrawImages = model.getTechnicalDrawingImages();
        String productManual = model.getProductManual();
        String productBorchure = model.getProductBrochure();

        // make the flag false if any of the property is invalid
        if( isPropertyInvalid(productPath) || isPropertyInvalid(productCoverImage) || isPropertyInvalid(productImages) || isPropertyInvalid(productColorImg) || isPropertyInvalid(productTechSpecImages) ||
                isPropertyInvalid(productTechDrawImages) || isPropertyInvalid(productManual) || isPropertyInvalid(productBorchure)){
            allPropertiesValid = false;
        }

        return allPropertiesValid;
    }

    private boolean isPropertyInvalid( String property) {
        return (resourceResolver.getResource(property) == null) ? true : false;
    }

    private boolean isPropertyInvalid( String[] property) {
        boolean invalidProp = false;
        if(property != null && property.length > 0 ){
            for(String prop : property){
                if(isPropertyInvalid(prop)){
                    invalidProp = true;
                }
            }
        }
        return invalidProp;
    }
}
