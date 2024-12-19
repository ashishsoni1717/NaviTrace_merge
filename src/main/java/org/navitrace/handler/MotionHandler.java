
package org.navitrace.handler;

import jakarta.inject.Inject;
import org.navitrace.config.Keys;
import org.navitrace.helper.model.AttributeUtil;
import org.navitrace.model.Position;
import org.navitrace.session.cache.CacheManager;

public class MotionHandler extends BasePositionHandler {

    private final CacheManager cacheManager;

    @Inject
    public MotionHandler(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void handlePosition(Position position, Callback callback) {
        if (!position.hasAttribute(Position.KEY_MOTION)) {
            double threshold = AttributeUtil.lookup(
                    cacheManager, Keys.EVENT_MOTION_SPEED_THRESHOLD, position.getDeviceId());
            position.set(Position.KEY_MOTION, position.getSpeed() > threshold);
        }
        callback.processed(false);
    }

}
