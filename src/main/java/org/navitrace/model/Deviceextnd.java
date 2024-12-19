package org.navitrace.model;

import org.navitrace.storage.StorageName;

import java.util.Date;

@StorageName("tc_deviceextnds")
public class Deviceextnd extends ExtendedModel{
    String name;
    String seatingCap;
    String model;
    String serviceType;
    String permitNo;
    String ownerName;
    String rtoName;
    Date permitValidFrom;
    Date permitValidTo;
    String permitFrom;
    String permitTo;
    long period;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeatingCap() {
        return seatingCap;
    }

    public void setSeatingCap(String seatingCap) {
        this.seatingCap = seatingCap;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getPermitNo() {
        return permitNo;
    }

    public void setPermitNo(String permitNo) {
        this.permitNo = permitNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getRtoName() {
        return rtoName;
    }

    public void setRtoName(String rtoName) {
        this.rtoName = rtoName;
    }

    public Date getPermitValidFrom() {
        return permitValidFrom;
    }

    public void setPermitValidFrom(Date permitValidFrom) {
        this.permitValidFrom = permitValidFrom;
    }

    public Date getPermitValidTo() {
        return permitValidTo;
    }

    public void setPermitValidTo(Date permitValidTo) {
        this.permitValidTo = permitValidTo;
    }

    public String getPermitFrom() {
        return permitFrom;
    }

    public void setPermitFrom(String permitFrom) {
        this.permitFrom = permitFrom;
    }

    public String getPermitTo() {
        return permitTo;
    }

    public void setPermitTo(String permitTo) {
        this.permitTo = permitTo;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }
}
