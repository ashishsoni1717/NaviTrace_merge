package org.navitrace.model;
import org.navitrace.storage.StorageName;

import java.util.Date ;

@StorageName("tc_oems")
public class Oem extends BaseModel {
    private String name ;
    private String contact ;
    private String address ;
    private Date empanelledOn ;
    private Date empanelledUpto ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getEmpanelledOn() {
        return empanelledOn;
    }

    public void setEmpanelledOn(Date empanelledOn) {
        this.empanelledOn = empanelledOn;
    }

    public Date getEmpanelledUpto() {
        return empanelledUpto;
    }

    public void setEmpanelledUpto(Date empanelledUpto) {
        this.empanelledUpto = empanelledUpto;
    }
}
