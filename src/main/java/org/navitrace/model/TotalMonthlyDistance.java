package org.navitrace.model;

public class TotalMonthlyDistance {
    private  long tcmonth;
    private long deviceid;
    private double totalDistance;

    public long getTcmonth() {
        return tcmonth;
    }

    public void setTcmonth(long tcmonth) {
        this.tcmonth = tcmonth;
    }

    public double getTotalDistance() {
        return totalDistance;
    }
    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }
    public long getDeviceid() {
        return deviceid;
    }
    public void setDeviceid(long deviceid) {
        this.deviceid = deviceid;
    }
}
