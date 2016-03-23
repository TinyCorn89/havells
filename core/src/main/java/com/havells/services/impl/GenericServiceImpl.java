package com.havells.services.impl;

import com.havells.services.GenericService;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(metatype = true)
@Service
@org.apache.felix.scr.annotations.Properties({
        @Property(name = "service.vendor", value = "Havells India"),
        @Property(name = "service.description", value = "A generic service.")
})
public class GenericServiceImpl implements GenericService {


    @Reference
    private ResourceResolverFactory resolverFactory;
    @Reference
    private ConfigurationAdmin configAdmin;

   protected static final Logger LOG = LoggerFactory.getLogger(GenericServiceImpl.class);

    @Override
    public ResourceResolver getAdminResourceResolver() {
        ResourceResolver resourceResolver = null;
        try {
            Map<String,Object> authInfo = new HashMap<String,Object>();
            authInfo.put(ResourceResolverFactory.SUBSERVICE, "adminResourceResolver");
            resourceResolver = resolverFactory.getServiceResourceResolver(authInfo);
        } catch(LoginException exe) {
            LOG.error("Exception while getting resource resolver." + exe);
        }
        return resourceResolver;
    }

    @Override
    public String getProps(String pid,String key){
        try {
            if(configAdmin != null){
                Configuration conf = configAdmin.getConfiguration(pid);
                if(conf != null){
                     return (String) conf.getProperties().get(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
