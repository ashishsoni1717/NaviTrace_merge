package org.navitrace.model;

import org.navitrace.storage.QueryIgnore;
import org.navitrace.storage.StorageName;

import java.util.Date;

@StorageName("tc_audittrails")
public class Audittrail extends BaseModel {

    Date createdon ;
    long   userid ;
    String username ;
    String component ;
    String activity ;
    String statuscode;
    String platform ;
    String description ;
    String ipaddress;


    public Date getCreatedon() {
        return createdon;
    }

    public void setCreatedon(Date createdon) {
        this.createdon = createdon;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(String statuscode) {
        this.statuscode = statuscode;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

//    @QueryIgnore
//    public String getUserName(){
//        if(getUserid() != 0 && Context.getUsersManager().getById(getUserid())!=null){
//            return Context.getUsersManager().getById(getUserid()).getName();
//        }
//        return null;
//    }
}
