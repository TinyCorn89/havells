package com.havells.services.impl

import com.day.cq.commons.jcr.JcrConstants
import com.day.cq.search.PredicateGroup
import com.day.cq.search.Query
import com.day.cq.search.QueryBuilder
import com.day.cq.search.result.Hit
import com.day.cq.search.result.SearchResult
import com.havells.core.model.product.ProductConstant
import com.havells.services.SearchQuery
import org.apache.commons.lang.time.StopWatch
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Service
import org.apache.sling.api.resource.Resource
import org.apache.sling.api.resource.ResourceResolver
import org.apache.sling.jcr.resource.JcrResourceConstants
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.jcr.Session

@Component(metatype = true)
@Service
public class SearchQueryImpl implements SearchQuery {

    private static final Logger LOG = LoggerFactory.getLogger(SearchQueryImpl.class);

    final String PRICE_FILTER = "price"
    final String GROUP_FILTER = "group."
    int universalCounter;

    /**
     *
     * @param filters
     * @param isFilterInRange
     * @param resolver
     * @return
     */
    @Override
    List<Resource> getProducts(String filters, boolean isFilterInRange, String commercePath, ResourceResolver resolver) {
        universalCounter = 2;
        QueryBuilder queryBuilder = resolver.adaptTo(QueryBuilder.class);
        Map<String, String> finalQueryMap = getFinalQueryMap(filters, commercePath);
        LOG.info("The Query for getReportData: {}"+ finalQueryMap.toString());

        Query query = queryBuilder.createQuery(PredicateGroup.create(finalQueryMap),resolver.adaptTo(Session.class));
        SearchResult result = query.getResult();

        List<Resource> resourceList = new ArrayList<>();
        for (Hit hit : result.getHits()) {
            resourceList.add(resolver.getResource(hit.getPath()));
        }

    return resourceList

    }

    private def final ALL_FILTERS_SEPARATOR = ';'
    private def final FILTER_KEY_VALUE_SEPARATOR = '||'
    private def final PRICE_RANGE_SEPARATOR = '-'
    private def final FILTER_VALUES_SEPARATOR = ','


    private synchronized Map<String, String> getFinalQueryMap(final String filters,final String commercePath) {

        Map<String, String> finalQueryMap = new LinkedHashMap<String, String>();
        finalQueryMap.put("path", commercePath);
        finalQueryMap.put("type", JcrConstants.NT_UNSTRUCTURED);
        finalQueryMap.put("1_property", JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY);
        finalQueryMap.put("1_property.value", ProductConstant.HAVELLS_PRODUCTS_RESOURCE_TYPE);

        List<String> tokens = filters.tokenize(ALL_FILTERS_SEPARATOR)
        for (String token : tokens) {
            if (token) {
                List<String> filterType = token.tokenize(FILTER_KEY_VALUE_SEPARATOR)
                if (PRICE_FILTER.equalsIgnoreCase(filterType.get(0)))
                    finalQueryMap.putAll(getPriceQueryMap(filterType, filterType.get(1).tokenize(FILTER_VALUES_SEPARATOR)));
                else{
                    finalQueryMap.putAll(getNonPriceQueryMap(filterType, filterType.get(1).tokenize(FILTER_VALUES_SEPARATOR)));
                }
            }
        }
        finalQueryMap.put("p.limit","-1");
        return finalQueryMap;
    }

    private Map<String, String> getNonPriceQueryMap(List<String> filterType, List keyValuesList) {

        Map nonPriceQueryMap = new LinkedHashMap();
        nonPriceQueryMap.put(universalCounter + "_property","otherInfo/technicalSpec/*/key");

        //TODO : temporary condition added for color property. Have to handle lowecase uppercase for selected filter key
        if("colour".equalsIgnoreCase(filterType.get(0)) || "color".equalsIgnoreCase(filterType.get(0))){
            nonPriceQueryMap.put(universalCounter + "_property.1_value","%" + "colour" + "%");
            nonPriceQueryMap.put(universalCounter + "_property.2_value","%" + "color" + "%");
            nonPriceQueryMap.put(universalCounter + "_property.3_value","%" + "Colour" + "%");
            nonPriceQueryMap.put(universalCounter + "_property.4_value","%" + "Color" + "%");
        }else{
            nonPriceQueryMap.put(universalCounter + "_property","otherInfo/technicalSpec/*/key");
            nonPriceQueryMap.put(universalCounter + "_property.value","%" + filterType.get(0) + "%");
        }

        nonPriceQueryMap.put(universalCounter + "_property.operation","like");
        universalCounter ++;
        nonPriceQueryMap.put(universalCounter +"_property","otherInfo/technicalSpec/*/value");

        boolean  isFacetNumerical = false;
        if(keyValuesList.size() > 0){
            isFacetNumerical = isOnlyNumerical(keyValuesList.get(0));
        }

        if (isMultipleFacetsSelected(filterType)) {

            int listSize = keyValuesList.size();
            for (int count = 0; count < listSize; count++) {
                int tempCount = count + 1;
                if ( isFacetNumerical ){
                    nonPriceQueryMap.put(universalCounter + "_property." + tempCount + "_value", keyValuesList.get(count));
                }else{
                    nonPriceQueryMap.put(universalCounter + "_property." + tempCount + "_value","%" + keyValuesList.get(count) + "%");
                }
            }
        } else {
            if (isFacetNumerical){
                nonPriceQueryMap.put(universalCounter + "_property.value", filterType.get(1));
            }else{
                nonPriceQueryMap.put(universalCounter + "_property.value","%" + filterType.get(1) + "%");
            }
        }

        if ( !isFacetNumerical ) {
            nonPriceQueryMap.put(universalCounter + "_property.operation", "like");
        }
        universalCounter++;
        return nonPriceQueryMap;
    }

    private Map<String, String> getPriceQueryMap(List<String> filterType, List keyValuesList) {

        Map priceQueryMap = new LinkedHashMap();
        if (isMultipleFacetsSelected(filterType)) {
            int listSize = keyValuesList.size()
            for (int count = 0; count < listSize; count++) {
                List<String> priceRange = keyValuesList.get(count).tokenize(PRICE_RANGE_SEPARATOR);
                priceQueryMap.put(GROUP_FILTER + universalCounter + "_rangeproperty.property",PRICE_FILTER);
                priceQueryMap.put(GROUP_FILTER + universalCounter + "_rangeproperty.lowerBound",priceRange.get(0));
                priceQueryMap.put(GROUP_FILTER + universalCounter + "_rangeproperty.lowerOperation",">=");
                priceQueryMap.put(GROUP_FILTER + universalCounter + "_rangeproperty.upperBound",priceRange.get(1));
                priceQueryMap.put(GROUP_FILTER + universalCounter + "_rangeproperty.upperOperation","<=");
                priceQueryMap.put(GROUP_FILTER + universalCounter + "_rangeproperty.decimal","true");
                universalCounter ++;

            }
            priceQueryMap.put("group.p.or","true");
        } else {
            List<String> priceRange = filterType[1].tokenize(PRICE_RANGE_SEPARATOR);
            priceQueryMap.put("rangeproperty.property",PRICE_FILTER);
            priceQueryMap.put("rangeproperty.lowerBound",priceRange.get(0));
            priceQueryMap.put("rangeproperty.lowerOperation",">=");
            priceQueryMap.put("rangeproperty.upperBound",priceRange.get(1));
            priceQueryMap.put("rangeproperty.upperOperation","<=");
            priceQueryMap.put("rangeproperty.decimal","true");
        }
        return priceQueryMap;
    }

    private boolean isMultipleFacetsSelected(List<String> filterType) {
        return filterType.get(1).contains(FILTER_VALUES_SEPARATOR);
    }

    public boolean isOnlyNumerical(String str){
        return str.matches("\\d+");
    }
}
