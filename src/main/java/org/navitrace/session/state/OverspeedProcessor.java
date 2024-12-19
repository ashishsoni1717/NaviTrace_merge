
package org.navitrace.session.state;

import org.navitrace.model.Event;
import org.navitrace.model.Position;

public final class OverspeedProcessor {

    public static final String ATTRIBUTE_SPEED = "speed";

    private OverspeedProcessor() {
    }

    public static void updateState(
            OverspeedState state, Position position,
            double speedLimit, double multiplier, long minimalDuration, long geofenceId) {

        state.setEvent(null);

        boolean oldState = state.getOverspeedState();
        if (oldState) {
            boolean newState = position.getSpeed() > speedLimit * multiplier;
            if (newState) {
                checkEvent(state, position, speedLimit, minimalDuration);
            } else {
                state.setOverspeedState(false);
                state.setOverspeedTime(null);
                state.setOverspeedGeofenceId(0);
            }
        } else if (position != null && position.getSpeed() > speedLimit * multiplier) {
            state.setOverspeedState(true);
            state.setOverspeedTime(position.getFixTime());
            state.setOverspeedGeofenceId(geofenceId);

            checkEvent(state, position, speedLimit, minimalDuration);
        }
    }

    private static void checkEvent(OverspeedState state, Position position, double speedLimit, long minimalDuration) {
        if (state.getOverspeedTime() != null) {
            long oldTime = state.getOverspeedTime().getTime();
            long newTime = position.getFixTime().getTime();
            if (newTime - oldTime >= minimalDuration) {

                Event event = new Event(Event.TYPE_DEVICE_OVERSPEED, position);
                event.set(ATTRIBUTE_SPEED, position.getSpeed());
                event.set(Position.KEY_SPEED_LIMIT, speedLimit);
                event.setGeofenceId(state.getOverspeedGeofenceId());

                state.setOverspeedTime(null);
                state.setOverspeedGeofenceId(0);
                state.setEvent(event);

            }
        }
    }
}
