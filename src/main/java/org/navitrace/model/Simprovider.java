package org.navitrace.model;

import org.navitrace.storage.StorageName;

@StorageName("tc_simproviders")
public class Simprovider extends  BaseModel{

    private String name ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
