
package org.navitrace.handler.events;

import jakarta.inject.Inject;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.model.Event;
import org.navitrace.model.Position;
import org.navitrace.session.cache.CacheManager;

public class AlertEventHandler extends BaseEventHandler {

    private final CacheManager cacheManager;
    private final boolean ignoreDuplicateAlerts;

    @Inject
    public AlertEventHandler(Config config, CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        ignoreDuplicateAlerts = config.getBoolean(Keys.EVENT_IGNORE_DUPLICATE_ALERTS);
    }

    @Override
    public void analyzePosition(Position position, Callback callback) {
        Object alarm = position.getAttributes().get(Position.KEY_ALARM);
        if (alarm != null) {
            boolean ignoreAlert = false;
            if (ignoreDuplicateAlerts) {
                Position lastPosition = cacheManager.getPosition(position.getDeviceId());
                if (lastPosition != null && alarm.equals(lastPosition.getAttributes().get(Position.KEY_ALARM))) {
                    ignoreAlert = true;
                }
            }
            if (!ignoreAlert) {
                Event event = new Event(Event.TYPE_ALARM, position);
                event.set(Position.KEY_ALARM, (String) alarm);
                callback.eventDetected(event);
            }
        }
    }

}
