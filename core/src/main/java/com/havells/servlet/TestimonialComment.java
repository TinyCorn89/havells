package com.havells.servlet;

import com.havells.services.TestimonialAddService;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Intelligrape
 * Date: 2/8/15
 * Time: 2:55 PM
 * To change this template use File | Settings | File Templates.
 */
@SlingServlet(
        methods = {"POST"},
        paths = {"/bin/addTestimonialComment"},
        extensions = {"html"}
)
@Properties({
        @Property(name = "service.description", value = "Add Comment Servlet", propertyPrivate = false),
        @Property(name = "service.vendor", value = "Havells", propertyPrivate = false)
})
public class TestimonialComment extends SlingAllMethodsServlet {
    private static final String UTF_8 = "UTF-8";
    private final String COMMENT_COMPONENT = "havells/components/commerce/productDetails/productTestiMonials/comment";

    @Reference
    private TestimonialAddService addService;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setContentType("application/html");
        try {
            String productPath = java.net.URLDecoder.decode(request.getParameter("productPath"), UTF_8);

            Map<String, Object> commentPropertyMap = new HashMap<String, Object>();
            commentPropertyMap.put("commentDesc", java.net.URLDecoder.decode(request.getParameter("comment"), UTF_8));
            commentPropertyMap.put("commentRating", request.getParameter("rating"));
            commentPropertyMap.put("commentDate", getCurrentDate());
            commentPropertyMap.put("userName", java.net.URLDecoder.decode(request.getParameter("fullName"), UTF_8));
            commentPropertyMap.put("fileReference", java.net.URLDecoder.decode(request.getParameter("avatar"), UTF_8));
            commentPropertyMap.put("sling:resourceType", COMMENT_COMPONENT);
            commentPropertyMap.put("jcr:primaryType", "cq:Comment");

            addService.addTestimonial(productPath, commentPropertyMap, request.getResourceResolver());

            response.getWriter().write("saved comment successfully.");

        } catch (Exception e) {
            response.getWriter().write("problem saving comment");
        }
    }

    public String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
