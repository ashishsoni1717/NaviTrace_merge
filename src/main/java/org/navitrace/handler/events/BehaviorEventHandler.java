
package org.navitrace.handler.events;

import jakarta.inject.Inject;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.helper.UnitsConverter;
import org.navitrace.model.Event;
import org.navitrace.model.Position;
import org.navitrace.session.cache.CacheManager;

public class BehaviorEventHandler extends BaseEventHandler {

    private final double accelerationThreshold;
    private final double brakingThreshold;

    private final CacheManager cacheManager;

    @Inject
    public BehaviorEventHandler(Config config, CacheManager cacheManager) {
        accelerationThreshold = config.getDouble(Keys.EVENT_BEHAVIOR_ACCELERATION_THRESHOLD);
        brakingThreshold = config.getDouble(Keys.EVENT_BEHAVIOR_BRAKING_THRESHOLD);
        this.cacheManager = cacheManager;
    }

    @Override
    public void analyzePosition(Position position, Callback callback) {

        Position lastPosition = cacheManager.getPosition(position.getDeviceId());
        if (lastPosition != null && position.getFixTime().equals(lastPosition.getFixTime())) {
            double acceleration = UnitsConverter.mpsFromKnots(position.getSpeed() - lastPosition.getSpeed()) * 1000
                    / (position.getFixTime().getTime() - lastPosition.getFixTime().getTime());
            if (accelerationThreshold != 0 && acceleration >= accelerationThreshold) {
                Event event = new Event(Event.TYPE_ALARM, position);
                event.set(Position.KEY_ALARM, Position.ALARM_ACCELERATION);
                callback.eventDetected(event);
            } else if (brakingThreshold != 0 && acceleration <= -brakingThreshold) {
                Event event = new Event(Event.TYPE_ALARM, position);
                event.set(Position.KEY_ALARM, Position.ALARM_BRAKING);
                callback.eventDetected(event);
            }
        }
    }

}
