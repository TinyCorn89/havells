package com.havells.supportUtils;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.havells.supportUtils.ScriptConstants;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.*;
import javax.jcr.query.QueryManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SlingServlet(paths = {"/bin/map/productImages"},
        methods = {"GET"}, generateComponent = false)
@Component(label = "PIM Image mapping correction", enabled = true, metatype = false, immediate = true)
public class PimImagePathMapperServlet extends SlingAllMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(PimImagePathMapperServlet.class);

    private static final String IMAGE_FILE_EXT = ".png";
    private static final String TECH_SPECS_IMAGE_NAME = "techspec";
    private static final String TECH_DRAW_IMAGE_NAME = "techdrawing";
    private static final String TECH_FEATURE_IMAGE_NAME = "feature";
    private static final String ACCESSORY_IMAGE_NAME = "Accessories" ;


    ResourceResolver resolver;
    QueryBuilder queryBuilder;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        resolver = request.getResourceResolver();
        queryBuilder = resolver.adaptTo(QueryBuilder.class);
        String searchFileName = request.getParameter("searchFileName") != null ?
                request.getParameter("searchFileName"): "cover"+IMAGE_FILE_EXT;

        String jcrDamPath = request.getParameter("jcrDamPath");
        try {

            renderAllImagePaths(jcrDamPath != null ? jcrDamPath : "/content/dam/havells", searchFileName);
            response.getWriter().write("Total number of products images corrected : " + counter);

        } catch (Exception ex) {
            LOGGER.info("repository exception "+ex);
            ex.printStackTrace();
        }finally {
            if(resolver != null && resolver.isLive()) resolver.close();
        }
       // response.getWriter().write("something went wrong, please check the logs..");
    }

    /**
     *
     * @param path
     * @param imageName
     * @throws RepositoryException
     */
    private void renderAllImagePaths(String path, String imageName) throws RepositoryException {

        Map<String, String> parameter = new HashMap<String, String>();
        parameter.put("path", path);
        parameter.put("type", "dam:Asset");
        parameter.put("nodename", imageName);

        QueryManager queryManager = resolver.adaptTo(Session.class).getWorkspace().getQueryManager();
        String xpathQuery = "/jcr:root"+path+"//*[jcr:contains(., '"+imageName+"')] order by @jcr:score";
        javax.jcr.query.Query query = queryManager.createQuery(xpathQuery, javax.jcr.query.Query.XPATH);

        NodeIterator nodeIterator = query.execute().getNodes();
        while (nodeIterator.hasNext()) {
            Node node = nodeIterator.nextNode();
            foundProductsCounter++;
            LOGGER.info(node.getPath());
            if(!(node.getPath().contains("folderThumbnail"))){
                findImageUrl(node.getParent().getPath(), node.getParent().getName());
            }
        }
        LOGGER.info("total no of products found with cover.png : "+foundProductsCounter);
        LOGGER.info("total no of products images changed : "+counter);
    }
    int foundProductsCounter = 0;
    int counter = 0;

    /**
     *
     * @param skuId
     * @throws RepositoryException
     */
    private void findImageUrl(String imagePath, String skuId) throws RepositoryException {
        Map<String, String> parameter = new HashMap<String, String>();

        parameter.put("path", "/etc/commerce/products/havells");
        parameter.put("type", "nt:unstructured");
        parameter.put("property", "sling:resourceType");
        parameter.put("property.value", "commerce/components/product");
        parameter.put("nodename", skuId.toLowerCase());


        Query query = queryBuilder.createQuery(PredicateGroup.create(parameter), resolver.adaptTo(Session.class));
        query.setStart(0);

        SearchResult result = query.getResult();
        List<Hit> hits = result.getHits();
        LOGGER.info("-------------------------------------"+skuId+"----------------------------------------------------------");
        if (hits.size() > 0) {
            LOGGER.info("image found ");
            for(Hit hit : hits){
                LOGGER.info(hit.getPath());
                try {
                    Resource productResource = hit.getResource();
                    Node resNode = productResource.adaptTo(Node.class);
                    setProductDocs(resNode, imagePath);
                    setProductImages(resNode, imagePath);
                    setTechSpecsImages(resNode, imagePath);
                    setTechDrawingImages(resNode, imagePath);
                    setDetailsFeatureImages(productResource, imagePath);
                    setAccessoryFeaturesImages(productResource, imagePath);
                    counter++;
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                resolver.adaptTo(Session.class).save();
            }
        } else {
            LOGGER.info("image not found ");
        }
        LOGGER.info("-----------------------------------------------------------------------------------------------");
    }

    /**
     * Setting  feature1.png, feature2.png etc.
     * @param productResource
     * @param imagePath
     * @throws RepositoryException
     */
    private void setDetailsFeatureImages(Resource productResource, String imagePath) throws RepositoryException {
          Resource detailsFeatureNode = productResource.getChild(ScriptConstants.OTHER_INFO+ ScriptConstants.DETAIL_FEATURES);
          ValueMap valueMap = detailsFeatureNode.adaptTo(ValueMap.class);
          Node featureNode = detailsFeatureNode.adaptTo(Node.class);
          for (int counter = 1; counter <= ScriptConstants.MAX_DETAIL_FEATURE_PROPERTIES ; counter++){
                  if (valueMap.containsKey("image"+counter)){
                      if(!"".equals(featureNode.getProperty("image"+counter).getString().trim())) {
                          featureNode.setProperty("image" + counter, imagePath + "/" + TECH_FEATURE_IMAGE_NAME + counter + IMAGE_FILE_EXT);
                      }
                  }else{
                      break;
                  }
          }
    }

    /**
     * Setting cover.png, color.png, manual doc pdf, brochure pdf etc.
     * @param resNode
     * @param imagePath
     * @throws RepositoryException
     */
    private void setProductDocs(Node resNode, String imagePath) throws RepositoryException {

        resNode.setProperty(ScriptConstants.COLOR_IMAGE_PROP, imagePath + "/color"+IMAGE_FILE_EXT);
        resNode.setProperty(ScriptConstants.PRODUCT_MANUAL_DOC_PROP, imagePath + "/product-manual.pdf");
        resNode.setProperty(ScriptConstants.PRODUCT_BROCHURE_PROP, imagePath + "/product-brochure.pdf");
        //setting productImages
        if(resNode.hasNode("image")) {
            Node coverImage = resNode.getNode("image");
            coverImage.setProperty(ScriptConstants.COVER_IMAGE_PROP, imagePath + "/cover" + IMAGE_FILE_EXT);
        }
    }

    /**
     * Setting 1.png, 2.png, 3.png etc.
     * @param resNode
     * @param imagePath
     * @throws RepositoryException
     */
    private void setProductImages(Node resNode, String imagePath) throws RepositoryException{

        Value[] productImages = resNode.getProperty(ScriptConstants.PRODUCT_IMAGES_PROP).getValues();
        String[] str = new String[productImages.length];
        for (int count = 1; count <= productImages.length; count++) {
            str[count - 1] = imagePath + "/" + count + IMAGE_FILE_EXT;
        }
        resNode.setProperty(ScriptConstants.PRODUCT_IMAGES_PROP, str);

    }

    /**
     * Setting techspecs2.png, techspecs2.png etc.
     * @param resNode
     * @param imagePath
     * @throws RepositoryException
     */
    private void setTechSpecsImages(Node resNode, String imagePath) throws RepositoryException{

        //Setting TechSpecs images
        Node techSpecs = resNode.getNode(ScriptConstants.OTHER_INFO+ ScriptConstants.TECHNICAL_SPECIFICATION);
        Value[] techSpecsImages = techSpecs.getProperty(ScriptConstants.TECH_SPECS_IMAGES_PROP).getValues();
        String[] techSpecsPath = new String[techSpecsImages.length];
        for (int count = 1; count <= techSpecsPath.length; count++) {
            techSpecsPath[count - 1] = imagePath + "/" + TECH_SPECS_IMAGE_NAME + count + IMAGE_FILE_EXT;
        }
        techSpecs.setProperty(ScriptConstants.TECH_SPECS_IMAGES_PROP, techSpecsPath);

    }

    /**
     * Setting techdrawing1.png, techdrawing2.png etc
     * @param resNode
     * @param imagePath
     * @throws RepositoryException
     */
    private void setTechDrawingImages(Node resNode, String imagePath) throws RepositoryException{
        //Setting technical drawing images :drawingImages

        Node drawingImageNode = resNode.getNode(ScriptConstants.OTHER_INFO+ ScriptConstants.TECHNICAL_DRAWING);
        Value[] drawingImages = drawingImageNode.getProperty(ScriptConstants.TECH_DRAWING_IMAGES_PROP).getValues();
        String[] drawingImagesPath = new String[drawingImages.length];
        for (int count = 1; count <= drawingImagesPath.length; count++) {
            drawingImagesPath[count - 1] = imagePath + "/" + TECH_DRAW_IMAGE_NAME+ count + IMAGE_FILE_EXT;
        }
        drawingImageNode.setProperty(ScriptConstants.TECH_DRAWING_IMAGES_PROP, drawingImagesPath);
    }

    private void setAccessoryFeaturesImages(Resource productResource, String imagePath) throws RepositoryException {

        Resource detailsFeatureResource = productResource.getChild(ScriptConstants.OTHER_INFO + ScriptConstants.ACCESSORY);
        if(detailsFeatureResource!=null)  {
            ValueMap valueMap = detailsFeatureResource.adaptTo(ValueMap.class);
            Node featureNode = detailsFeatureResource.adaptTo(Node.class);
            for (int counter = 1; counter <= ScriptConstants.MAX_ACCESSORY_IMAGES; counter++) {
                if (valueMap.containsKey("image" + counter)) {
                    if (!"".equals(featureNode.getProperty("image" + counter).getString().trim())) {
                        featureNode.setProperty("image" + counter, imagePath + "/" + ACCESSORY_IMAGE_NAME + counter + IMAGE_FILE_EXT);
                    }
                }
            }
        }
    }
}
