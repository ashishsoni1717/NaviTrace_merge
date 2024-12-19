package org.navitrace.model;

import org.navitrace.storage.QueryIgnore;
import org.navitrace.storage.StorageName;

import java.util.Date;

@StorageName("tc_eventnotificationlogs")
public class Eventnotificationlog extends ExtendedModel {

    private long userid ;
    private long deviceid ;
    private String alerttype ;
    private Date eventtime ;
    private long positionid;
    private double latitude;
    private double longitude;
    private double altitude; // value in meters
    private Date start_time ;
    private double start_lat ;
    private double start_lng ;
    private String status ;
    private String notificator_type ;

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public long getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(long deviceid) {
        this.deviceid = deviceid;
    }

    public String getAlerttype() {
        return alerttype;
    }

    public void setAlerttype(String alerttype) {
        this.alerttype = alerttype;
    }

    public Date getEventtime() {
        return eventtime;
    }

    public void setEventtime(Date eventtime) {
        this.eventtime = eventtime;
    }

    public long getPositionid() {
        return positionid;
    }

    public void setPositionid(long positionid) {
        this.positionid = positionid;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public double getStart_lat() {
        return start_lat;
    }

    public void setStart_lat(double start_lat) {
        this.start_lat = start_lat;
    }

    public double getStart_lng() {
        return start_lng;
    }

    public void setStart_lng(double start_lng) {
        this.start_lng = start_lng;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotificator_type() {
        return notificator_type;
    }

    public void setNotificator_type(String notificator_type) {
        this.notificator_type = notificator_type;
    }

    private String devicename;

//    @QueryIgnore
//    public String getDevicename() {
//        if(deviceid!=0 && Context.getDeviceManager().getById(deviceid) != null){
//            this.devicename = Context.getDeviceManager().getById(deviceid).getName() ;
//        }else{
//            this.devicename = null ;
//        }
//        return devicename;
//    }

    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    private String sendTo ;

//    @QueryIgnore
//    public String getSendTo() {
//        if(userid!=0 && Context.getUsersManager().getById(userid) != null){
//            this.sendTo = Context.getUsersManager().getById(userid).getName() ;
//        }else{
//            this.sendTo = null ;
//        }
//        return sendTo;
//    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

//    @QueryIgnore
//    public String getUserName(){
//        if(getUserid() != 0 && Context.getUsersManager().getById(getUserid())!=null){
//            return Context.getUsersManager().getById(getUserid()).getName();
//        }
//        return null;
//    }
}

