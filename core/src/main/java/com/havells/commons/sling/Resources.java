package com.havells.commons.sling;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.Iterator;

public class Resources {

    public static Iterable<Resource> listChildren(final Resource resource) {
        return new Iterable<Resource>() {
            @Override
            public Iterator<Resource> iterator() {
                return (resource != null) ? resource.listChildren() : (Iterator<Resource>) new ArrayList().iterator();
            }
        };
    }

    /**
     * Checks if specified URL resolves to an internal page.
     *
     * @param url      the specified URL
     * @param resolver {@code ResourceResolver} to use to resolve URL
     * @return true if specified URL resolves to an internal page, false otherwise
     */
    public static boolean isInternalURL(String url, ResourceResolver resolver) {
        Resource resource = (resolver != null) ? resolver.getResource(url) : null;
        return (resource != null && resource.adaptTo(Page.class) != null);
    }

    public static boolean isValidResourcePath(String url, ResourceResolver resolver) {
        return !resolver.resolve(url).getResourceType().equals(Resource.RESOURCE_TYPE_NON_EXISTING);
    }

    public static String getValidURL(ResourceResolver resourceResolver, String path){
        if( "".equals(path.trim())) {
            return "#";
        } else{
            Resource pageResource = resourceResolver.getResource(path);
            if (pageResource != null && pageResource.adaptTo(Page.class) != null) {
                path = path + ".html";
            }
            return path;
        }
    }

    /**
     * Checks whether the given property has multiple values or single value. Returns false if the resource passed is null.
     * @param resource
     * @param toCheckProperty
     * @return
     * @throws RepositoryException
     */
    public static boolean isPropertyMultiple(Resource resource, String toCheckProperty) throws RepositoryException {
        if(resource !=null) {
            Node node = resource.adaptTo(Node.class);
            if(node.hasProperty(toCheckProperty)) return node.getProperty(toCheckProperty).isMultiple();
        }
        return false;
    }

    /**
     * Checks whether the given resource is a valid DAM asset. Returns false if the resource is null
     * @param resource
     * @param image
     * @return
     * @throws RepositoryException
     */
    public static boolean checkIfValid(Resource resource,String image) throws RepositoryException {
        boolean isResourceValid = false;
        if(resource != null){
            Resource imageResource = resource.getResourceResolver().getResource(image);
            if( imageResource != null){
                Asset imageAsset = imageResource.adaptTo(Asset.class);
                if(imageAsset != null && DamUtil.isImage(imageAsset)){
                    isResourceValid = true;
                }
            }
        }
        return isResourceValid;
    }
}
