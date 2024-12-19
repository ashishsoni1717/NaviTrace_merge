
package org.navitrace.handler;

import jakarta.inject.Inject;
import org.navitrace.model.Position;
import org.navitrace.session.cache.CacheManager;

import java.util.Date;

public class OutdatedHandler extends BasePositionHandler {

    private final CacheManager cacheManager;

    @Inject
    public OutdatedHandler(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void handlePosition(Position position, Callback callback) {
        if (position.getOutdated()) {
            Position last = cacheManager.getPosition(position.getDeviceId());
            if (last != null) {
                position.setFixTime(last.getFixTime());
                position.setValid(last.getValid());
                position.setLatitude(last.getLatitude());
                position.setLongitude(last.getLongitude());
                position.setAltitude(last.getAltitude());
                position.setSpeed(last.getSpeed());
                position.setCourse(last.getCourse());
                position.setAccuracy(last.getAccuracy());
            } else {
                position.setFixTime(new Date(315964819000L)); // gps epoch 1980-01-06
            }
            if (position.getDeviceTime() == null) {
                position.setDeviceTime(position.getServerTime());
            }
        }
        callback.processed(false);
    }

}
