package com.havells.core.model;

import com.havells.services.TestimonialAddService;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Intelligrape
 * Date: 2/8/15
 * Time: 9:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class FetchTestimonials {

    private static final Logger LOG = LoggerFactory.getLogger(FetchTestimonials.class);

    private String pagePath;
    private Resource resource;
    private String filterBy = "Top Rated";

    public FetchTestimonials(Resource resource, String pagePath, String filterBy){
        this.pagePath = pagePath;
        this.resource = resource;
        if(filterBy!=null){
            this.filterBy = filterBy;
        }
    }

    public List<Resource> getComments(){
        List<Resource> comments = new ArrayList<Resource>();
        try{
            Resource commentResource = resource.getResourceResolver().getResource(TestimonialAddService.USER_GENERATED_CONTENT+pagePath+"/jcr:content/comments");
            if(commentResource != null){
                Iterator<Resource> resourceIt = commentResource.listChildren();
                while(resourceIt.hasNext()){
                    comments.add(resourceIt.next());
                }
            }
        }catch(Exception e){
            LOG.error("exception occurred in fetching nodes" + e);
        }
        Collections.reverse(comments);
        if(filterBy.equals("Top Rated")){
            comments = sortList(comments);
        }
        return comments;
    }

    List<Resource> sortList(List<Resource> list){
        Collections.sort(list, new Comparator<Resource>() {

            @Override
            public int compare(Resource r1, Resource r2) {
                Integer value = null;
                try {
                    Double rating1 = Double.valueOf(r1.getValueMap().get("commentRating").toString());
                    Double rating2 = Double.valueOf(r2.getValueMap().get("commentRating").toString());
                    value = rating2.compareTo(rating1);
                } catch (Exception exp) {
                    LOG.error("Exception during returning list" + exp);
                }
                return value;              }
        });
        return list;

    }
}
