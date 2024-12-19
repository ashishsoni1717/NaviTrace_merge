
package org.navitrace.handler.events;

import jakarta.inject.Inject;
import org.navitrace.helper.model.PositionUtil;
import org.navitrace.model.Device;
import org.navitrace.model.Event;
import org.navitrace.model.Position;
import org.navitrace.session.cache.CacheManager;

public class IgnitionEventHandler extends BaseEventHandler {

    private final CacheManager cacheManager;

    @Inject
    public IgnitionEventHandler(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void analyzePosition(Position position, Callback callback) {
        Device device = cacheManager.getObject(Device.class, position.getDeviceId());
        if (device == null || !PositionUtil.isLatest(cacheManager, position)) {
            return;
        }

        if (position.hasAttribute(Position.KEY_IGNITION)) {
            boolean ignition = position.getBoolean(Position.KEY_IGNITION);

            Position lastPosition = cacheManager.getPosition(position.getDeviceId());
            if (lastPosition != null && lastPosition.hasAttribute(Position.KEY_IGNITION)) {
                boolean oldIgnition = lastPosition.getBoolean(Position.KEY_IGNITION);

                if (ignition && !oldIgnition) {
                    callback.eventDetected(new Event(Event.TYPE_IGNITION_ON, position));
                } else if (!ignition && oldIgnition) {
                    callback.eventDetected(new Event(Event.TYPE_IGNITION_OFF, position));
                }
            }
        }
    }

}
