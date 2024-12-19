package org.navitrace.model;

import org.navitrace.storage.StorageName;

@StorageName("tc_alarmtypes")
public class Alarmtype extends BaseModel {

    private long nid;
    private String type;

    public long getNid() {
        return nid;
    }

    public void setNid(long nid) {
        this.nid = nid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
