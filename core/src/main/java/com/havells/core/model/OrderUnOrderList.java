package com.havells.core.model;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import java.util.ArrayList;
import java.util.List;

public class OrderUnOrderList {

    private Resource resource;
    public OrderUnOrderList(Resource res){
        this.resource = res;
    }
    public List getList(){
        List<String> list = new ArrayList<String>();
        if(resource != null){
            ValueMap valueMap = resource.adaptTo(ValueMap.class);
            String[] strings = valueMap.get("pages", String[].class);
            if(strings != null && strings.length > 0){
                for(String temp : strings){
                    list.add(temp);
                }
            }
        }
        return list;
    }
}
