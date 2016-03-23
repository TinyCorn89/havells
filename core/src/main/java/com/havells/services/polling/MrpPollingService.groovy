package com.havells.services.polling

import com.day.cq.polling.importer.ImportException
import com.day.cq.polling.importer.Importer
import com.day.cq.replication.Replicator
import com.havells.core.model.product.ProductConstant
import groovy.util.logging.Slf4j
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Service
import org.apache.sling.api.resource.Resource
import org.apache.sling.api.resource.ResourceResolver
import org.apache.sling.api.resource.ValueMap
import org.apache.sling.commons.json.JSONArray
import org.apache.sling.commons.json.JSONObject
import com.day.cq.search.result.Hit
import com.day.cq.search.result.SearchResult
import com.day.cq.search.PredicateGroup
import com.day.cq.search.Query
import com.day.cq.search.QueryBuilder
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.*;
import javax.jcr.query.QueryManager

/**
 * Importer.SCHEME_PROPERTY is used to register an importer service such that it can be
 * matched with a configuration node.
 */
@Service(value = Importer.class)
@Component(metatype = false)
@Property(name = Importer.SCHEME_PROPERTY, value = "mrp-importer-scheme", propertyPrivate = true)
class MrpPollingService implements Importer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MrpPollingService.class);
    private final String RESPONSE_ROOT = "LT_MRP_DETAILS"

    private final SERVICE_PRICE_KEY = "price"
    private final SERVICE_SKU_KEY = "sku"

    @org.apache.felix.scr.annotations.Reference
    IMrpImporterService mrpImporterService

    @org.apache.felix.scr.annotations.Reference
    Replicator replicator

    @Override
    void importData(String scheme, String dataSource, Resource target, String login, String password) throws ImportException {
        LOGGER.info "import data with five parameters "
        initMRPWebServiceCall(scheme, dataSource, target)
    }
    @Override
    void importData(String scheme, String dataSource, Resource target) throws ImportException {
        initMRPWebServiceCall(scheme, dataSource, target)
    }

    private void initMRPWebServiceCall(String scheme, String dataSource, Resource target){

        ResourceResolver resourceResolver = target.getResourceResolver()
        try {
            String serviceResponse = mrpImporterService.getProductPriceInfo()
            LOGGER.info " Response is {} ", serviceResponse
            JSONObject jsonObj = new JSONObject(serviceResponse)
            LOGGER.info "JSON Response is {} ", jsonObj
            JSONArray jsonArray = (JSONArray) jsonObj.getJSONArray(RESPONSE_ROOT)
            if(jsonArray)
                extractMRPResponse(resourceResolver, jsonArray);

        } catch (Exception exception) {
            LOGGER.info "exception occurs " + exception
        } finally {
            if (resourceResolver) resourceResolver.close()
        }
    }
    /**
     *
     * @param resolver
     * @param jsonArray
     */
    private void extractMRPResponse(ResourceResolver resolver, JSONArray jsonArray){

        javax.jcr.Session jcrSession = resolver.adaptTo(Session.class)

        (0..jsonArray.length() - 1).each { index ->
            String price = jsonArray.getJSONObject(index).get(SERVICE_PRICE_KEY)
            String skuCode = jsonArray.getJSONObject(index).get(SERVICE_SKU_KEY)

            LOGGER.info "price {}", price
            LOGGER.info "skuCode {}", skuCode

            Resource productResource = findProductSKU(jcrSession, skuCode)
            if(productResource)
                updateProductPrice(productResource, jcrSession, skuCode, price)

        }
    }

    private Resource findProductSKU(Session jcrSession, final String skuCode){
        Map<String, String> parameter = new HashMap<String, String>();

        parameter.put("path", "/etc/commerce/products/havells")
        parameter.put("type", "nt:unstructured")
        parameter.put("property", "sling:resourceType")
        parameter.put("property.value", "commerce/components/product")
        parameter.put("nodename", skuCode.toLowerCase())

        Query query = queryBuilder.createQuery(PredicateGroup.create(parameter), jcrSession)
        query.setStart(0)

        SearchResult result = query.getResult()
        List<Hit> hits = result.getHits()
        if (hits.size() > 0) {
            for (Hit hit : hits) {
                return hit.getResource()
            }
            LOGGER.info("{} found ", skuCode)
        } else {
            generateReport(skuCode)
        }
        return null
    }

    private void updateProductPrice(final Resource productResource, Session jcrSession,
                                    final String sku, final String price){

        LOGGER.info("{} being updated here ", skuCode);
        ResourceResolver resolver = productResource.getResourceResolver()
        try{
              javax.jcr.Node productNode = productResource.adoptTo(Node)
              productNode.setProperty(ProductConstant.PRODUCT_PRICE_PROP, getPrice(price))
              jcrSession.save()

        }catch (Exception ex){
        }
        finally{
            if (resolver != null && resolver.isLive()) resolver.close();
        }

    }

    private void replicationProduct(final String productPath, Session jcrSession ){

    }

    private Double getPrice(String price){
            return Double.parseDouble(price)
    }
    private void generateReport(final String skuCode){
        LOGGER.info("{} not found ", skuCode);
    }
}
