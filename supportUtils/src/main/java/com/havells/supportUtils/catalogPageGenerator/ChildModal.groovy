package com.havells.servlet.catalogPageGenerator

/**
 * Created by jitendra on 08/08/15.
 */
public class ChildModal {

    public String path
    public String name

    ChildModal(String path, String name){
        this.path = path
        this.name = name
    }

    String getPath() {
        return path
    }

    void setPath(String path) {
        this.path = path
    }

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }
}
