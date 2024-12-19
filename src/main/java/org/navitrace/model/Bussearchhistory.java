package org.navitrace.model;

import org.navitrace.storage.StorageName;

import java.util.Date;

@StorageName("tc_bussearchhistorys")
public class Bussearchhistory extends BaseModel{
    long userId;
    String locations;
    Date searchOn;
    String time;
    Date createdOn;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public Date getSearchOn() {
        return searchOn;
    }

    public void setSearchOn(Date searchOn) {
        this.searchOn = searchOn;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
