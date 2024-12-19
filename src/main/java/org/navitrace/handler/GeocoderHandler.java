
package org.navitrace.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.geocoder.Geocoder;
import org.navitrace.model.Position;
import org.navitrace.session.cache.CacheManager;

public class GeocoderHandler extends BasePositionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeocoderHandler.class);

    private final Geocoder geocoder;
    private final CacheManager cacheManager;
    private final boolean ignorePositions;
    private final boolean processInvalidPositions;
    private final int reuseDistance;

    public GeocoderHandler(Config config, Geocoder geocoder, CacheManager cacheManager) {
        this.geocoder = geocoder;
        this.cacheManager = cacheManager;
        ignorePositions = config.getBoolean(Keys.GEOCODER_IGNORE_POSITIONS);
        processInvalidPositions = config.getBoolean(Keys.GEOCODER_PROCESS_INVALID_POSITIONS);
        reuseDistance = config.getInteger(Keys.GEOCODER_REUSE_DISTANCE, 0);
    }

    @Override
    public void handlePosition(Position position, Callback callback) {
        if (!ignorePositions && (processInvalidPositions || position.getValid())) {
            if (reuseDistance != 0) {
                Position lastPosition = cacheManager.getPosition(position.getDeviceId());
                if (lastPosition != null && lastPosition.getAddress() != null
                        && position.getDouble(Position.KEY_DISTANCE) <= reuseDistance) {
                    position.setAddress(lastPosition.getAddress());
                    callback.processed(false);
                    return;
                }
            }

            geocoder.getAddress(position.getLatitude(), position.getLongitude(),
                    new Geocoder.ReverseGeocoderCallback() {
                @Override
                public void onSuccess(String address) {
                    position.setAddress(address);
                    callback.processed(false);
                }

                @Override
                public void onFailure(Throwable e) {
                    LOGGER.warn("Geocoding failed", e);
                    callback.processed(false);
                }
            });
        } else {
            callback.processed(false);
        }
    }

}
