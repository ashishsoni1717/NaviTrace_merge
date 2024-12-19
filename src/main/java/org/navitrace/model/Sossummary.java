package org.navitrace.model;

import org.navitrace.storage.StorageName;

@StorageName("tc_sossummarys")
public class Sossummary  extends BaseModel{
    private long deviceId;
    private long tcMonth ;
    private long year ;
    private String status ;
    private long total ;

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public long getTcMonth() {
        return tcMonth;
    }

    public void setTcMonth(long tcMonth) {
        this.tcMonth = tcMonth;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

}
