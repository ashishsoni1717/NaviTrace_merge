
package org.navitrace.session.state;

import org.navitrace.model.Device;
import org.navitrace.model.Event;

import java.util.Date;

public class OverspeedState {

    public static OverspeedState fromDevice(Device device) {
        OverspeedState state = new OverspeedState();
        state.overspeedState = device.getOverspeedState();
        state.overspeedTime = device.getOverspeedTime();
        state.overspeedGeofenceId = device.getOverspeedGeofenceId();
        return state;
    }

    public void toDevice(Device device) {
        device.setOverspeedState(overspeedState);
        device.setOverspeedTime(overspeedTime);
        device.setOverspeedGeofenceId(overspeedGeofenceId);
    }

    private boolean changed;

    public boolean isChanged() {
        return changed;
    }

    private boolean overspeedState;

    public boolean getOverspeedState() {
        return overspeedState;
    }

    public void setOverspeedState(boolean overspeedState) {
        this.overspeedState = overspeedState;
        changed = true;
    }

    private Date overspeedTime;

    public Date getOverspeedTime() {
        return overspeedTime;
    }

    public void setOverspeedTime(Date overspeedTime) {
        this.overspeedTime = overspeedTime;
        changed = true;
    }

    private long overspeedGeofenceId;

    public long getOverspeedGeofenceId() {
        return overspeedGeofenceId;
    }

    public void setOverspeedGeofenceId(long overspeedGeofenceId) {
        this.overspeedGeofenceId = overspeedGeofenceId;
        changed = true;
    }

    private Event event;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

}
