package com.havells.servlet;

import com.havells.services.RatingService;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import java.io.IOException;

@SlingServlet(
        methods = {"GET"},
        paths={"/bin/productRating"},
        extensions = {"html"}
)
@Properties({
        @Property(name="service.description",value="Display Rating Servlet", propertyPrivate=false),
        @Property(name="service.vendor",value="Havells", propertyPrivate=false)
})
public class RatingComponent extends SlingAllMethodsServlet{

    @Reference
    private RatingService ratingService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setContentType("application/html");
        try{
            String productPath = java.net.URLDecoder.decode(request.getParameter("productPath"),"UTF-8");
            String rating = ratingService.fetchRating(productPath,request.getResourceResolver());
            response.getWriter().write(rating);
        }catch(Exception e){
            response.getWriter().write("problem fetching rating");
        }
    }
}
