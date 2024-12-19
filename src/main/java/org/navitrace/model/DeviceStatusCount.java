package org.navitrace.model;

import org.navitrace.storage.StorageName;

@StorageName("tc_devices")
public class DeviceStatusCount {
    private String status;
    private long count;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
