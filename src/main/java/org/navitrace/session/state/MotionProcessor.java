
package org.navitrace.session.state;

import org.navitrace.model.Event;
import org.navitrace.model.Position;
import org.navitrace.reports.common.TripsConfig;

public final class MotionProcessor {

    private MotionProcessor() {
    }

    public static void updateState(
            MotionState state, Position position, boolean newState, TripsConfig tripsConfig) {

        state.setEvent(null);

        boolean oldState = state.getMotionState();
        if (oldState == newState) {
            if (state.getMotionTime() != null) {
                long oldTime = state.getMotionTime().getTime();
                long newTime = position.getFixTime().getTime();

                double distance = position.getDouble(Position.KEY_TOTAL_DISTANCE) - state.getMotionDistance();
                Boolean ignition = null;
                if (tripsConfig.getUseIgnition() && position.hasAttribute(Position.KEY_IGNITION)) {
                    ignition = position.getBoolean(Position.KEY_IGNITION);
                }

                boolean generateEvent = false;
                if (newState) {
                    if (newTime - oldTime >= tripsConfig.getMinimalTripDuration()
                            || distance >= tripsConfig.getMinimalTripDistance()) {
                        generateEvent = true;
                    }
                } else {
                    if (newTime - oldTime >= tripsConfig.getMinimalParkingDuration()
                            || ignition != null && !ignition) {
                        generateEvent = true;
                    }
                }

                if (generateEvent) {

                    String eventType = newState ? Event.TYPE_DEVICE_MOVING : Event.TYPE_DEVICE_STOPPED;
                    Event event = new Event(eventType, position);

                    state.setMotionStreak(newState);
                    state.setMotionTime(null);
                    state.setMotionDistance(0);
                    state.setEvent(event);

                }
            }
        } else {
            state.setMotionState(newState);
            if (state.getMotionStreak() == newState) {
                state.setMotionTime(null);
                state.setMotionDistance(0);
            } else {
                state.setMotionTime(position.getFixTime());
                state.setMotionDistance(position.getDouble(Position.KEY_TOTAL_DISTANCE));
            }
        }
    }

}
