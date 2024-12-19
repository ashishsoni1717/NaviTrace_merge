
package org.navitrace.forward;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.navitrace.model.Device;
import org.navitrace.model.Event;
import org.navitrace.model.Geofence;
import org.navitrace.model.Maintenance;
import org.navitrace.model.Position;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventData {

    private Event event;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

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

    private Geofence geofence;

    public Geofence getGeofence() {
        return geofence;
    }

    public void setGeofence(Geofence geofence) {
        this.geofence = geofence;
    }

    private Maintenance maintenance;

    public Maintenance getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Maintenance maintenance) {
        this.maintenance = maintenance;
    }

}
