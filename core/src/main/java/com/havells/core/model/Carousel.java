package com.havells.core.model;

import org.apache.sling.api.resource.Resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Carousel {

    private int noOfSlides;
    private List<Resource> childList;

    public final int getNoOfSlides() {
        return noOfSlides;
    }

    public final void setNoOfSlides(int noOfSlides) {
        this.noOfSlides = noOfSlides;
    }

    public final List<Resource> getChildList() {
        return childList;
    }

    public Carousel(Resource resource) {

        childList = new ArrayList<Resource>();
        if (resource != null) {
            Iterator<Resource> children = resource.listChildren();
            while (children.hasNext()) {
                childList.add(children.next());
            }
        }
        noOfSlides = childList.size();
    }
}
