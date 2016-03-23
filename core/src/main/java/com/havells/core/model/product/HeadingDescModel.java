package com.havells.core.model.product;

import com.day.cq.tagging.Tag;

public class HeadingDescModel {

    private String heading;
    private String description;
    private String imagePath;
    private Tag tag;

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Tag getTag(){
        return tag;
    }

    public void setTag(Tag tag){
        this.tag = tag;
    }
    HeadingDescModel(){ /* EMPTY CONSTRUCTOR */ }

    HeadingDescModel(String heading, String description) {
        this.heading = heading;
        this.description = description;
    }

    HeadingDescModel(String heading, String description, String imagePath) {
        this.heading = heading;
        this.description = description;
        this.imagePath = imagePath;
    }

    HeadingDescModel(String heading, String description,Tag tag) {
        this.heading = heading;
        this.description = description;
        this.tag = tag;
    }

}
