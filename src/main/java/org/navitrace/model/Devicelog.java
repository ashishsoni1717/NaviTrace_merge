package org.navitrace.model;

import org.navitrace.storage.StorageName;

import java.util.Date;

@StorageName("tc_devicelogs")
public class Devicelog extends BaseModel {

    private long deviceId;
    private Date createdOn;
    private String hexCode;
    private String content;
    private String ip;
    private Date fixTime;

    public long getDeviceId() {
        return deviceId;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public String getHexCode() {
        return hexCode;
    }

    public void setHexCode(String hexCode) {
        this.hexCode = hexCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

//    @QueryIgnore
//    public String getDeivceName(){
//        if(getDeviceId() != 0 && Context.getDeviceManager().getById(getDeviceId())!=null){
//            return Context.getDeviceManager().getById(getDeviceId()).getName();
//        }
//        return null;
//    }

    public Date getFixTime() {
        return fixTime;
    }

    public void setFixTime(Date fixTime) {
        this.fixTime = fixTime;
    }
}
