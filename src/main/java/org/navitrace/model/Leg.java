package org.navitrace.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.navitrace.config.Keys;
import org.navitrace.geofence.GeofencePolyline;
import org.navitrace.helper.UnitsConverter;
import org.navitrace.storage.QueryIgnore;
import org.navitrace.storage.StorageName;

import java.text.ParseException;

@StorageName("tc_legs")
public class Leg extends BaseModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(Leg.class);

    public static final String KEY_NAME = "name" ;
    public static final String KEY_POINT = "point" ;
    public static final String KEY_LATITUDE = "latitude" ;
    public static final String KEY_LONGITUDE = "longitude" ;
    public static final String KEY_RADIUS = "radius" ;

    private String nameTo;
    private String nameFrom;

    public String getNameFrom() {
        return nameFrom;
    }

    public void setNameFrom(String nameFrom) {
        this.nameFrom = nameFrom;
    }

    public String getNameTo() {
        return nameTo;
    }

    public void setNameTo(String nameTo) {
        this.nameTo = nameTo;
    }

    private long routeId;

//    @QueryExtended
    public long getRouteId() {
        return routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }

    private int routeIndex;

//    @QueryExtended
    public int getRouteIndex() {
        return routeIndex;
    }

    public void setRouteIndex(int routeIndex) {
        this.routeIndex = routeIndex;
    }

    private double latitudeFrom;

//    @QueryExtended
    public double getLatitudeFrom() {
        return latitudeFrom;
    }

    public void setLatitudeFrom(double latitudeFrom) {
        this.latitudeFrom = latitudeFrom;
    }

    private double longitudeFrom;

//    @QueryExtended
    public double getLongitudeFrom() {
        return longitudeFrom;
    }

    public void setLongitudeFrom(double longitudeFrom) {
        this.longitudeFrom = longitudeFrom;
    }

    private double radiusFrom;

    public double getRadiusFrom() {
        return radiusFrom;
    }

    public void setRadiusFrom(double radiusFrom) {
        this.radiusFrom = radiusFrom;
    }

    private double latitudeTo;

//    @QueryExtended
    public double getLatitudeTo() {
        return latitudeTo;
    }

    public void setLatitudeTo(double latitudeTo) {
        this.latitudeTo = latitudeTo;
    }

    private double longitudeTo;

//    @QueryExtended
    public double getLongitudeTo() {
        return longitudeTo;
    }

    public void setLongitudeTo(double longitudeTo) {
        this.longitudeTo = longitudeTo;
    }

    private double radiusTo;

    public double getRadiusTo() {
        return radiusTo;
    }

    public void setRadiusTo(double radiusTo) {
        this.radiusTo = radiusTo;
    }

//    @QueryExtended
    public String getPolyline() {
        return geofence.toWkt();
    }

//    public void setPolyline(String polyline) {
//        try {
//            geofence = new GeofencePolyline(polyline, Context.getConfig().getDouble(Keys.GEOFENCE_POLYLINE_DISTANCE));
//        } catch (ParseException e) {
//            LOGGER.warn("Failed to parse", e);
//            LOGGER.info("RouteId " + getRouteId());
//            LOGGER.info("Polyline " + polyline);
//        }
//    }

    private GeofencePolyline geofence;

    @QueryIgnore
    @JsonIgnore
    public GeofencePolyline getGeofence() {
        return geofence;
    }

    @QueryIgnore
    @JsonIgnore
    public void setGeofence(GeofencePolyline geofence) {
        this.geofence = geofence;
    }

    private double estDistanceFromOrigin;

    private long estdDurnFromOrigin;

    public double getEstDistanceFromOrigin() {
        return estDistanceFromOrigin;
    }

    public void setEstDistanceFromOrigin(double estDistanceFromOrigin) {
        this.estDistanceFromOrigin = estDistanceFromOrigin;
    }

    @JsonIgnore
    public long getEstdDurnFromOrigin() {
        return estdDurnFromOrigin;
    }

    public void setEstdDurnFromOrigin(long estdDurnFromOrigin) {
        this.estdDurnFromOrigin = estdDurnFromOrigin;
    }

    private String estDurationFromOrigin;

//    @QueryIgnore
//    public String getEstDurationFromOrigin(){
//        return UnitsConverter.secondsToHhMmSs(estdDurnFromOrigin);
//    }

    public void setEstDurationFromOrigin(String xyz){

    }
}
