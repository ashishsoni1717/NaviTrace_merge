
package org.navitrace.session.state;

import org.navitrace.model.Device;
import org.navitrace.model.Event;

import java.util.Date;

public class MotionState {

    public static MotionState fromDevice(Device device) {
        MotionState state = new MotionState();
        state.motionStreak = device.getMotionStreak();
        state.motionState = device.getMotionState();
        state.motionTime = device.getMotionTime();
        state.motionDistance = device.getMotionDistance();
        return state;
    }

    public void toDevice(Device device) {
        device.setMotionStreak(motionStreak);
        device.setMotionState(motionState);
        device.setMotionTime(motionTime);
        device.setMotionDistance(motionDistance);
    }

    private boolean changed;

    public boolean isChanged() {
        return changed;
    }

    private boolean motionStreak;

    public boolean getMotionStreak() {
        return motionStreak;
    }

    public void setMotionStreak(boolean motionStreak) {
        this.motionStreak = motionStreak;
        changed = true;
    }

    private boolean motionState;

    public boolean getMotionState() {
        return motionState;
    }

    public void setMotionState(boolean motionState) {
        this.motionState = motionState;
        changed = true;
    }

    private Date motionTime;

    public Date getMotionTime() {
        return motionTime;
    }

    public void setMotionTime(Date motionTime) {
        this.motionTime = motionTime;
        changed = true;
    }

    private double motionDistance;

    public double getMotionDistance() {
        return motionDistance;
    }

    public void setMotionDistance(double motionDistance) {
        this.motionDistance = motionDistance;
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
