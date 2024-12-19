package org.navitrace.model;

import org.navitrace.storage.QueryIgnore;
import org.navitrace.storage.StorageName;

@StorageName("tc_bustimetables")
public class Bustimetable extends ExtendedModel{
    long deviceextendid;
    long stationfromid;
    long stationtoid;
    String departureTime;
    String arrivalTime;
    long trip;
    String via;
    double distance;
    String address;

    public long getDeviceextendid() {
        return deviceextendid;
    }

    public void setDeviceextendid(long deviceextendid) {
        this.deviceextendid = deviceextendid;
    }

    public long getStationfromid() {
        return stationfromid;
    }

    public void setStationfromid(long stationfromid) {
        this.stationfromid = stationfromid;
    }

    public long getStationtoid() {
        return stationtoid;
    }

    public void setStationtoid(long stationtoid) {
        this.stationtoid = stationtoid;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public long getTrip() {
        return trip;
    }

    public void setTrip(long trip) {
        this.trip = trip;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

//    @QueryIgnore
//    public String getStationFrom(){
//        if(getStationfromid() != 0 && Context.getGeofenceManager().getById(getStationfromid())!=null){
//            return Context.getGeofenceManager().getById(getStationfromid()).getName();
//        }
//        return null;
//    }
//
//    @QueryIgnore
//    public String getStationTo(){
//        if(getStationtoid() != 0 && Context.getGeofenceManager().getById(getStationtoid())!=null){
//            return Context.getGeofenceManager().getById(getStationtoid()).getName();
//        }
//        return null;
//    }
//
//    @QueryIgnore
//    public String getDeviceName(){
//        if(getDeviceextendid() != 0 && Context.getBusTimeTableManager().getById(getDeviceextendid())!=null){
//            Deviceextnd deviceextnd = Context.getBusTimeTableManager().getById(getDeviceextendid());
//            return deviceextnd.getName();
//        }
//        return null;
//    }

    @Override
    public String toString() {
        return "Bustimetable{" +
                "deviceextendid=" + deviceextendid +
                ", stationfromid=" + stationfromid +
                ", stationtoid=" + stationtoid +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", trip=" + trip +
                ", via='" + via + '\'' +
                ", distance=" + distance +
                ", address='" + address + '\'' +
                '}';
    }
}
