
package org.navitrace.handler;

import jakarta.inject.Inject;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.model.Driver;
import org.navitrace.model.Position;
import org.navitrace.session.cache.CacheManager;

public class DriverHandler extends BasePositionHandler {

    private final CacheManager cacheManager;
    private final boolean useLinkedDriver;

    @Inject
    public DriverHandler(Config config, CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        useLinkedDriver = config.getBoolean(Keys.PROCESSING_USE_LINKED_DRIVER);
    }

    @Override
    public void handlePosition(Position position, Callback callback) {
        if (useLinkedDriver && !position.hasAttribute(Position.KEY_DRIVER_UNIQUE_ID)) {
            var drivers = cacheManager.getDeviceObjects(position.getDeviceId(), Driver.class);
            if (!drivers.isEmpty()) {
                position.set(Position.KEY_DRIVER_UNIQUE_ID, drivers.iterator().next().getUniqueId());
            }
        }
        callback.processed(false);
    }

}
