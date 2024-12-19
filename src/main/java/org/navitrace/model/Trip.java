package org.navitrace.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.navitrace.storage.QueryIgnore;
import org.navitrace.storage.StorageName;

import java.util.*;
import java.util.Calendar;

@StorageName("tc_trips")
public class Trip extends BaseModel {
    private static final Logger LOGGER = LoggerFactory.getLogger(Trip.class);

    public static final String STATUS_STARTED = "started";
    public static final String STATUS_FINISHED = "finished";
    public static final String LEG_START_TIME = "startTime";
    public static final String EXPECTED_TIME = "expectedTime";
    public static final String LEG_FINISH_TIME = "finishTime";
    public static final String LEG_TRIP_DEVIATED = "tripDeviated";
    public static final String LEG_DISTANCE_TRAVELED = "distanceTraveled";
    public static final String LEG_DURATION_TRAVELED = "durationTraveled";
    public static final String LEG_STATUS = "status";
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_FORCED_FINISHED = "forcedFinished";
    public static final String LEG_PLANNED_TIME = "plannedTime";
    public static final String LEG_PLANNED_DISTANCE = "plannedDistance";
    public static final String LEG_PLANNED_DURATION = "plannedDuration";
    public static final String LEG_INDEX = "legIndex";
    public static final String LEG_ACTUAL_TIME = "actualTime";
    public static final String LEG_ADDRESS_FROM = "addressFrom";
    public static final String LEG_ADDRESS_TO = "addressTo";
    public static final String LEG_LATITUDE_FROM = "latitudeFrom";
    public static final String LEG_LONGITUDE_FROM = "longitudeFrom";
    public static final String LEG_LATITUDE_TO = "latitudeTo";
    public static final String LEG_LONGITUDE_TO = "longitudeTo";

    private long routeId;

    public long getRouteId() {
        return routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }

    private long legId;

    public long getLegId() {
        return legId;
    }

    public void setLegId(long legId) {
        this.legId = legId;
    }

    private long deviceId;

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private Date assignedTime;

    public Date getAssignedTime() {
        return assignedTime;
    }

    public void setAssignedTime(Date assignedTime) {
        this.assignedTime = assignedTime;
    }

    private Date startTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    private Date endTime;

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    private boolean tripDelayed;

    public boolean getTripDelayed() {
        return tripDelayed;
    }

    public void setTripDelayed(boolean tripDelayed) {
        this.tripDelayed = tripDelayed;
    }

    private boolean deviated;

    public boolean getDeviated() {
        return deviated;
    }

    public void setDeviated(boolean deviated) {
        this.deviated = deviated;
    }

    private long assignedBy;
    private long modifiedBy ;
    private double initialOdometer;
    private double closingOdometer;
    private String remarks ;
    private String closingType;

//    @QueryExtended
    public long getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(long assignedBy) {
        this.assignedBy = assignedBy;
    }

    public long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public double getInitialOdometer() {
        return initialOdometer;
    }

    public void setInitialOdometer(double initialOdometer) {
        this.initialOdometer = initialOdometer;
    }

    public double getClosingOdometer() {
        return closingOdometer;
    }

    public void setClosingOdometer(double closingOdometer) {
        this.closingOdometer = closingOdometer;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getClosingType() {
        return closingType;
    }

    public void setClosingType(String closingType) {
        this.closingType = closingType;
    }

    private LinkedList<Map<String,Object>> tripLegs = new LinkedList<>();

    public LinkedList<Map<String, Object>> getTripLegs() {
        return tripLegs;
    }

    public void setTripLegs(LinkedList<Map<String, Object>> tripLegs) {
        this.tripLegs = tripLegs;
    }

    public void set(int index, String key, Boolean value) {
        Map<String, Object> data = tripLegs.get(index) ;
        if (value != null) {
            data.put(key, value);
        }
    }

    public void set(int index, String key, Byte value) {
        Map<String, Object> data = tripLegs.get(index) ;
        if (value != null) {
            data.put(key, value.intValue());
        }
    }

    public void set(int index, String key, Short value) {
        Map<String, Object> data = tripLegs.get(index) ;
        if (value != null) {
            data.put(key, value.intValue());
        }
    }

    public void set(int index, String key, Integer value) {
        Map<String, Object> data = tripLegs.get(index) ;
        if (value != null) {
            data.put(key, value);
        }
    }

    public void set(int index, String key, Long value) {
        Map<String, Object> data = tripLegs.get(index) ;
        if (value != null) {
            data.put(key, value);
        }
    }

    public void set(int index, String key, Float value) {
        Map<String, Object> data = tripLegs.get(index) ;
        if (value != null) {
            data.put(key, value.doubleValue());
        }
    }

    public void set(int index, String key, Double value) {
        Map<String, Object> data = tripLegs.get(index) ;
        if (value != null) {
            data.put(key, value);
        }
    }

    public void set(int index, String key, String value) {
        Map<String, Object> data = tripLegs.get(index) ;
        if (value != null && !value.isEmpty()) {
            data.put(key, value);
        }
    }

    public void add(int index, Map.Entry<String, Object> entry) {
        Map<String, Object> data = tripLegs.get(index) ;
        if (entry != null && entry.getValue() != null) {
            data.put(entry.getKey(), entry.getValue());
        }
    }

    public String getString(int index, String key) {
        if(index >= tripLegs.size()){
            return null ;
        }
        Map<String, Object> data = tripLegs.get(index) ;
        if (data.containsKey(key)) {
            return (String) data.get(key);
        } else {
            return null;
        }
    }

    public double getDouble(int index, String key) {
        if(index >= tripLegs.size()){
            return 0.0 ;
        }
        Map<String, Object> data = tripLegs.get(index) ;
        if (data.containsKey(key)) {
            return ((Number) data.get(key)).doubleValue();
        } else {
            return 0.0;
        }
    }

    public boolean getBoolean(int index, String key) {
        if(index >= tripLegs.size()){
            return false ;
        }
        Map<String, Object> data = tripLegs.get(index) ;
        if (data.containsKey(key)) {
            return (Boolean) data.get(key);
        } else {
            return false;
        }
    }

    public int getInteger(int index, String key) {
        if(index >= tripLegs.size()){
            return 0 ;
        }
        Map<String, Object> data = tripLegs.get(index) ;
        if (data.containsKey(key)) {
            return ((Number) data.get(key)).intValue();
        } else {
            return 0;
        }
    }

    public long getLong(int index, String key) {
        if(index >= tripLegs.size()){
            return 0 ;
        }
        Map<String, Object> data = tripLegs.get(index) ;
        if (data.containsKey(key)) {
            return ((Number) data.get(key)).longValue();
        } else {
            return 0;
        }
    }

    public void sumDistance(int index, String key, double distance){
        double totalDistance = getDouble(index, key)  + distance;
        set(index, key, totalDistance) ;
    }

    public void sumDuration(int index, String key, long duration){
        long totalDuration = getLong(index, key)  + duration;
        set(index, key, totalDuration) ;
    }

//    @QueryIgnore
//    public String getVehicleNo(){
//        if(getDeviceId() != 0 && Context.getDeviceManager().getById(getDeviceId())!=null){
//            return Context.getDeviceManager().getById(getDeviceId()).getName();
//        }
//        return null;
//    }
//
//    @QueryIgnore
//    public String getAssignedByName(){
//        if(getDeviceId() != 0 && Context.getUsersManager().getById(getAssignedBy())!=null){
//            return Context.getUsersManager().getById(getAssignedBy()).getName();
//        }
//        return null;
//    }
//
//    @QueryIgnore
//    public long getLegIndex(int index, String key, long legIndex){
//        return getLong(index, key);
//    }
//
//    @QueryIgnore
//    public long getPlannedTimeInMills(long plannedTime){
//        Calendar c = Calendar.getInstance();
//        c.setTime(new Date());
////        long plannedDateMills = getLong(0, Trip.STATUS_STARTED);
//        return c.getTimeInMillis() + plannedTime;
//    }

    public void initTripLegs(Route route) {
        for(Leg leg : route.getLegs()){
            Map tripLeg = new HashMap();
            tripLeg.put(Trip.LEG_INDEX, leg.getRouteIndex());
            tripLeg.put(Trip.LEG_START_TIME, 0);
            tripLeg.put(Trip.LEG_FINISH_TIME, 0);
            tripLeg.put(Trip.LEG_TRIP_DEVIATED, false);
            tripLeg.put(Trip.LEG_PLANNED_DISTANCE, leg.getEstDistanceFromOrigin());
            tripLeg.put(Trip.LEG_PLANNED_TIME,0);  // getPlannedTimeInMills(leg.getEstdDurnFromOrigin())
            tripLeg.put(Trip.LEG_PLANNED_DURATION, leg.getEstdDurnFromOrigin());
            tripLeg.put(Trip.LEG_DISTANCE_TRAVELED,0);
            tripLeg.put(Trip.LEG_DURATION_TRAVELED,0);
            tripLeg.put(Trip.LEG_LATITUDE_FROM,leg.getLatitudeFrom());
            tripLeg.put(Trip.LEG_LONGITUDE_FROM,leg.getLongitudeFrom());
            tripLeg.put(Trip.LEG_LATITUDE_TO,leg.getLatitudeTo());
            tripLeg.put(Trip.LEG_LONGITUDE_TO,leg.getLatitudeTo());
            tripLeg.put(Trip.LEG_ADDRESS_FROM, "AddressFrom"); // RoutesHandlerHelper.getAddress(
                                                              // leg.getLatitudeFrom(),leg.getLongitudeFrom()
            tripLeg.put(Trip.LEG_ADDRESS_TO, "AddressTo");

            tripLeg.put(Trip.LEG_STATUS,Trip.STATUS_PENDING);
            tripLegs.add(tripLeg);
        }
    }
}