package org.navitrace.model;

import org.navitrace.storage.StorageName;

@StorageName("tc_rtos")
public class Rto extends  BaseModel{

    private String name;
    private String code;
    private long stateId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getStateId() {
        return stateId;
    }

    public void setStateId(long stateId) {
        this.stateId = stateId;
    }
}
