
package org.navitrace.reports.model;

import org.navitrace.model.Device;
import org.navitrace.model.Position;

public class DeviceReportItem {

    public DeviceReportItem(Device device, Position position) {
        this.device = device;
        this.position = position;
    }

    private Device device;

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    private Position position;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

}
