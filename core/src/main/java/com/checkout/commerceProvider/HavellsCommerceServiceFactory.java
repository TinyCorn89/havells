package com.checkout.commerceProvider;

import com.adobe.cq.commerce.api.CommerceService;
import com.adobe.cq.commerce.api.CommerceServiceFactory;
import com.adobe.cq.commerce.common.AbstractJcrCommerceServiceFactory;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;

/**
 * Created by intelligrape on 15/4/15.
 */
@Component
@Service
@Properties(value = {
        @Property(name = "service.description", value = "Factory for reference implementation commerce service"),
        @Property(name = "commerceProvider", value = "havells", propertyPrivate = true)
})
public class HavellsCommerceServiceFactory extends AbstractJcrCommerceServiceFactory implements CommerceServiceFactory {

    /**
     * Create a new <code>GeoCommerceServiceImpl</code>.
     */
    public CommerceService getCommerceService(Resource res) {
        return new HavellsCommerceServiceImpl(getServiceContext(), res);
    }
}
