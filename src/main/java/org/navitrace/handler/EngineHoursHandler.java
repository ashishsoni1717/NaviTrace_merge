
package org.navitrace.handler;

import jakarta.inject.Inject;
import org.navitrace.model.Position;
import org.navitrace.session.cache.CacheManager;

public class EngineHoursHandler extends BasePositionHandler {

    private final CacheManager cacheManager;

    @Inject
    public EngineHoursHandler(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void handlePosition(Position position, Callback callback) {
        if (!position.hasAttribute(Position.KEY_HOURS)) {
            Position last = cacheManager.getPosition(position.getDeviceId());
            if (last != null) {
                long hours = last.getLong(Position.KEY_HOURS);
                if (last.getBoolean(Position.KEY_IGNITION) && position.getBoolean(Position.KEY_IGNITION)) {
                    hours += position.getFixTime().getTime() - last.getFixTime().getTime();
                }
                if (hours != 0) {
                    position.set(Position.KEY_HOURS, hours);
                }
            }
        }
        callback.processed(false);
    }

}
