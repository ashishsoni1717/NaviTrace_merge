
package org.navitrace.handler;

import jakarta.inject.Inject;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.helper.DistanceCalculator;
import org.navitrace.model.Position;
import org.navitrace.session.cache.CacheManager;

public class DistanceHandler extends BasePositionHandler {

    private final CacheManager cacheManager;

    private final boolean filter;
    private final int minError;
    private final int maxError;

    @Inject
    public DistanceHandler(Config config, CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        this.filter = config.getBoolean(Keys.COORDINATES_FILTER);
        this.minError = config.getInteger(Keys.COORDINATES_MIN_ERROR);
        this.maxError = config.getInteger(Keys.COORDINATES_MAX_ERROR);
    }

    @Override
    public void handlePosition(Position position, Callback callback) {

        double distance = 0.0;
        if (position.hasAttribute(Position.KEY_DISTANCE)) {
            distance = position.getDouble(Position.KEY_DISTANCE);
        }
        double totalDistance;
        Position last = cacheManager.getPosition(position.getDeviceId());
        if (last != null) {
            totalDistance = last.getDouble(Position.KEY_TOTAL_DISTANCE);
            if (!position.hasAttribute(Position.KEY_DISTANCE)) {
                distance = DistanceCalculator.distance(
                        position.getLatitude(), position.getLongitude(),
                        last.getLatitude(), last.getLongitude());
            }
            if (filter && last.getLatitude() != 0 && last.getLongitude() != 0) {
                boolean satisfiesMin = minError == 0 || distance > minError;
                boolean satisfiesMax = maxError == 0 || distance < maxError || position.getValid();
                if (!satisfiesMin || !satisfiesMax) {
                    position.setValid(last.getValid());
                    position.setLatitude(last.getLatitude());
                    position.setLongitude(last.getLongitude());
                    distance = 0;
                }
            }
        } else {
            totalDistance = 0.0;
        }
        position.set(Position.KEY_DISTANCE, distance);
        position.set(Position.KEY_TOTAL_DISTANCE, totalDistance + distance);

        callback.processed(false);
    }

}
