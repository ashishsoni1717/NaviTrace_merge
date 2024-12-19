
package org.navitrace.forward;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.navitrace.model.Device;
import org.navitrace.model.Position;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PositionData {

    private Position position;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    private Device device;

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

}
