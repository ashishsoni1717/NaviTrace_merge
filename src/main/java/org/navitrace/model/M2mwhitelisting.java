package org.navitrace.model;

import org.navitrace.storage.StorageName;

@StorageName("tc_m2mwhitelistings")
public class M2mwhitelisting extends BaseModel{

    private String m2mIp;
    private boolean disabled;

    public String getM2mIp() {
        return m2mIp;
    }

    public void setM2mIp(String m2mIp) {
        this.m2mIp = m2mIp;
    }

    public boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}