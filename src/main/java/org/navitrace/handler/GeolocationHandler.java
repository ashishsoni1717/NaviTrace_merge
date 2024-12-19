
package org.navitrace.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.database.StatisticsManager;
import org.navitrace.geolocation.GeolocationProvider;
import org.navitrace.model.Position;
import org.navitrace.session.cache.CacheManager;

public class GeolocationHandler extends BasePositionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeolocationHandler.class);

    private final GeolocationProvider geolocationProvider;
    private final CacheManager cacheManager;
    private final StatisticsManager statisticsManager;
    private final boolean processInvalidPositions;
    private final boolean reuse;
    private final boolean requireWifi;

    public GeolocationHandler(
            Config config, GeolocationProvider geolocationProvider, CacheManager cacheManager,
            StatisticsManager statisticsManager) {
        this.geolocationProvider = geolocationProvider;
        this.cacheManager = cacheManager;
        this.statisticsManager = statisticsManager;
        processInvalidPositions = config.getBoolean(Keys.GEOLOCATION_PROCESS_INVALID_POSITIONS);
        reuse = config.getBoolean(Keys.GEOLOCATION_REUSE);
        requireWifi = config.getBoolean(Keys.GEOLOCATION_REQUIRE_WIFI);
    }

    @Override
    public void handlePosition(Position position, Callback callback) {
        if ((position.getOutdated() || processInvalidPositions && !position.getValid())
                && position.getNetwork() != null
                && (!requireWifi || position.getNetwork().getWifiAccessPoints() != null)) {
            if (reuse) {
                Position lastPosition = cacheManager.getPosition(position.getDeviceId());
                if (lastPosition != null && position.getNetwork().equals(lastPosition.getNetwork())) {
                    updatePosition(
                            position, lastPosition.getLatitude(), lastPosition.getLongitude(),
                            lastPosition.getAccuracy());
                    callback.processed(false);
                    return;
                }
            }

            if (statisticsManager != null) {
                statisticsManager.registerGeolocationRequest();
            }

            geolocationProvider.getLocation(position.getNetwork(),
                    new GeolocationProvider.LocationProviderCallback() {
                @Override
                public void onSuccess(double latitude, double longitude, double accuracy) {
                    updatePosition(position, latitude, longitude, accuracy);
                    callback.processed(false);
                }

                @Override
                public void onFailure(Throwable e) {
                    LOGGER.warn("Geolocation network error", e);
                    callback.processed(false);
                }
            });
        } else {
            callback.processed(false);
        }
    }

    private void updatePosition(Position position, double latitude, double longitude, double accuracy) {
        position.set(Position.KEY_APPROXIMATE, true);
        position.setValid(true);
        position.setFixTime(position.getDeviceTime());
        position.setLatitude(latitude);
        position.setLongitude(longitude);
        position.setAccuracy(accuracy);
        position.setAltitude(0);
        position.setSpeed(0);
        position.setCourse(0);
    }

}
