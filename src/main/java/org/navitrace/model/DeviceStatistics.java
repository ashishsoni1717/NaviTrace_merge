package org.navitrace.model;

import org.navitrace.storage.StorageName;

@StorageName("tc_statistics")
public class DeviceStatistics {
    private String label;
    private long deviceCount;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(long deviceCount) {
        this.deviceCount = deviceCount;
    }
}
