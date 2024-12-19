package org.navitrace.model;

import org.navitrace.storage.StorageName;


@StorageName("tc_vcategorys")
public class Vcategory extends  BaseModel{

    private String name ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String allowedFrom;
    private String allowedTill;

    public String getAllowedTill() {
        return allowedTill;
    }

    public void setAllowedTill(String allowedTill) {
        this.allowedTill = allowedTill;
    }

    public String getAllowedFrom() {
        return allowedFrom;
    }

    public void setAllowedFrom(String allowedFrom) {
        this.allowedFrom = allowedFrom;
    }
}
