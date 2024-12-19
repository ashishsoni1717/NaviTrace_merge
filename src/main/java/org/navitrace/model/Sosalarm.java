
package org.navitrace.model;

import org.navitrace.config.Keys;
import org.navitrace.storage.QueryIgnore;
import org.navitrace.storage.StorageName;

import java.util.Date;

@StorageName("tc_sosalarms")
public class Sosalarm extends Message {

    public Sosalarm(){
    }

    public Sosalarm(Event event) {
        setType(event.getType());
        setPositionId(event.getPositionId());
        setDeviceId(event.getDeviceId());
        setEventTime(event.getEventTime());
        setStatus(SOS_PENDING);
        setAttributes(event.getAttributes());
        setGeofenceId(event.getGeofenceId());
        setMaintenanceId(event.getMaintenanceId());
    }

    public static final String SOS_PENDING     = "pending";
    public static final String SOS_ACKNOWLEDGED = "acknowledged";
    public static final String SOS_CANCELED = "cancelled";
    public static final String SOS_RESOLVED = "resolved";

    private Date eventTime;

    public Date getEventTime() {
        return eventTime;
    }
    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    private long positionId;

    public long getPositionId() {
        return positionId;
    }

    public void setPositionId(long positionId) {
        this.positionId = positionId;
    }

    private long geofenceId = 0;

    public long getGeofenceId() {
        return geofenceId;
    }

    public void setGeofenceId(long geofenceId) {
        this.geofenceId = geofenceId;
    }

    private long maintenanceId = 0;

    public long getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(long maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    private  String action ;

    private String remark ;

    private String status ;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String location ;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private Date updateOn ;

    public Date getUpdateOn() {
        return updateOn;
    }

    public void setUpdateOn(Date updateOn) {
        this.updateOn = updateOn;
    }

    private String vehicleNo ;

//    @QueryIgnore
//    public String getVehicleNo() {
//        if(getDeviceId()!=0 && Context.getDeviceManager().getById(getDeviceId()) != null){
//            this.vehicleNo = Context.getDeviceManager().getById(getDeviceId()).getName() ;
//        }else{
//            this.vehicleNo = null ;
//        }
//        return vehicleNo;
//    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    private double latitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    private double longitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private String permitHolder;

    private String permitHolderPhone;

    public String getPermitHolder() {
        return permitHolder;
    }

    public void setPermitHolder(String permitHolder) {
        this.permitHolder = permitHolder;
    }

    public String getPermitHolderPhone() {
        return permitHolderPhone;
    }

    public void setPermitHolderPhone(String permitHolderPhone) {
        this.permitHolderPhone = permitHolderPhone;
    }
}
