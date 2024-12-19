
package org.navitrace.helper.model;

import org.navitrace.config.Config;
import org.navitrace.model.Geofence;
import org.navitrace.model.Position;
import org.navitrace.session.cache.CacheManager;

import java.util.ArrayList;
import java.util.List;

public final class GeofenceUtil {

    private GeofenceUtil() {
    }

    public static List<Long> getCurrentGeofences(Config config, CacheManager cacheManager, Position position) {
        List<Long> result = new ArrayList<>();
        for (Geofence geofence : cacheManager.getDeviceObjects(position.getDeviceId(), Geofence.class)) {
            if (geofence.getGeometry().containsPoint(
                    config, geofence, position.getLatitude(), position.getLongitude())) {
                result.add(geofence.getId());
            }
        }
        return result;
    }

}
