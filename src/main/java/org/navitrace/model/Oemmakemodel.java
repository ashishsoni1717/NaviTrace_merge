package org.navitrace.model;


import org.navitrace.storage.QueryIgnore;
import org.navitrace.storage.StorageName;

@StorageName("tc_oemmakemodels")
public class Oemmakemodel extends BaseModel {
    private long oemId;
    private String make ;
    private String model ;
    private String name ;
    private String oemName;
    public long getOemId() {
        return oemId;
    }

    public void setOemId(long oemId) {
        this.oemId = oemId;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @QueryIgnore
    public String getName() {
        return this.make + "-" + this.model;
    }

    public void setName(String name) {
        this.name = name;
    }

//    @QueryIgnore
//    public String getOemName() {
//        /**** Code to Set empName, from the cache  ****/
//        if(oemId != 0 && Context.getOemManager().getById(oemId) != null){
//            oemName = Context.getOemManager().getById(oemId).getName() ;
//        }
//        return oemName;
//    }


    public void setOemName(String oemName) {
        this.oemName = oemName;
    }
}
