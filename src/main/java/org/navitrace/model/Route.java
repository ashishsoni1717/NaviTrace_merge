package org.navitrace.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.navitrace.helper.UnitsConverter;
import org.navitrace.storage.QueryIgnore;
import org.navitrace.storage.StorageName;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@StorageName("tc_routes")
public class Route extends BaseModel {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private List<HashMap<String, Object>> points;

    @QueryIgnore
    public List<HashMap<String, Object>> getPoints() {
        return points;
    }

    @QueryIgnore
    public void setPoints(List<HashMap<String, Object>> points) {
        this.points = points;
    }

    private List<Leg> legs;

    public int findLegIndex(long legId) {
        for (int i = 0; i < legs.size(); i++) {
            if (legs.get(i).getId() == legId) {
                return i;
            }
        }
        return -1;
    }

    @QueryIgnore
    public List<Leg> getLegs() {
        return legs;
    }

    @QueryIgnore
    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    private String stopType;
    private String routeType;
    private String description;

    public String getStopType() {
        return stopType;
    }

    public void setStopType(String stopType) {
        this.stopType = stopType;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getString(String key, int index) {
        Map<String, Object> point = points.get(index) ;
        if (point.containsKey(key)) {
            return (String) point.get(key);
        } else {
            return null;
        }
    }

    public double getDouble(String key, int index) {
        Map<String, Object> point = points.get(index) ;
        if (point.containsKey(key)) {
            return ((Number) point.get(key)).doubleValue();
        } else {
            return 0.0;
        }
    }

    private long createdBy;
    private long modifiedBy;
    private Date createdOn;
    private Date lastUpdate;
    private long numStoppage;

//    @QueryExtended
    public long getCreatedBy(){ return createdBy;}

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public long getModifiedBy(){
        return modifiedBy;
    }

    public void setModifiedBy(long modifiedBy){
        this.modifiedBy = modifiedBy;
    }

//    @QueryExtended
    public Date getCreatedOn() {
        if (createdOn != null) {
            return new Date(createdOn.getTime());
        } else {
            return null;
        }
    }

    public void setCreatedOn(Date createdOn) {
        if (createdOn != null) {
            this.createdOn = new Date(createdOn.getTime());
        } else {
            this.createdOn = null;
        }
    }
    public Date getLastUpdate() {
        if (lastUpdate != null) {
            return new Date(lastUpdate.getTime());
        } else {
            return null;
        }
    }
    //    @QueryExtended  : commente by Baba
    public void setLastUpdate(Date lastUpdate) {
        if (lastUpdate != null) {
            this.lastUpdate = new Date(lastUpdate.getTime());
        } else {
            this.lastUpdate = null;
        }
    }

    @QueryIgnore
    public long getNumStoppage(){ return numStoppage;}

    public void setNumStoppage(long numStoppage) { this.numStoppage = numStoppage;}

    private double estimatedDistance;

    private long estdDuration;

    public double getEstimatedDistance() {
        return estimatedDistance;
    }

    public void setEstimatedDistance(double estimatedDistance) {
        this.estimatedDistance = estimatedDistance;
    }

    @JsonIgnore
    public long getEstdDuration(){
        return estdDuration;
    }

    public void setEstdDuration(long estdDuration){
        this.estdDuration = estdDuration;
    }

    private String estimatedDuration;

//    @QueryIgnore
//    public String getEstimatedDuration(){
//        return UnitsConverter.secondsToHhMmSs(estdDuration);
//    }

    public void setEstimatedDuration(String xyz){

    }
}