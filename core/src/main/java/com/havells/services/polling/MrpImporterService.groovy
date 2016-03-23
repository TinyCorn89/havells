package com.havells.services.polling

import groovy.util.logging.Slf4j
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.HttpMethod
import org.apache.commons.httpclient.HttpStatus
import org.apache.commons.httpclient.methods.GetMethod
import org.apache.commons.httpclient.params.HttpMethodParams
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Service
import org.apache.sling.commons.json.JSONArray
import org.osgi.service.component.ComponentContext

@Slf4j
@Component(metatype = true)
@Service
@org.apache.felix.scr.annotations.Properties([
        @org.apache.felix.scr.annotations.Property(name = "serviceUrl", label = "Service Url",
                description = "provide havells middle layer service url to fetch MRP",
                value = ["http://qamkonnect.havells.com:8080/xmwcsdealermkonnect/rfctojson?bapiname=ZBAPI_CHANGE_MRP_UPDATE"])
])
public class MrpImporterService implements IMrpImporterService{

    private  String SERVICE_URL
    /**
     * @param componentContext
     * @throws Exception
     */
    protected void activate(ComponentContext componentContext) throws Exception {
        Dictionary props = componentContext.getProperties()
        log.info "importing url is " + SERVICE_URL
        SERVICE_URL = (String) props.get("serviceUrl")
    }

    @Override
    String getProductPriceInfo() {

        try {
            HttpClient client = new HttpClient()
            log.info "importing url is " + SERVICE_URL
            HttpMethod method = new GetMethod(SERVICE_URL)

            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                    new DefaultHttpMethodRetryHandler(3, false));

            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                log.info("Method failed: {} ", method.getStatusLine());
            }
            // Read the response body.
            byte[] responseBody = method.getResponseBody()
            // Deal with the response.
            return (new String(responseBody))
        }catch(Exception ex){
            ex.printStackTrace()
            throw new Exception()
        }
    }
}
