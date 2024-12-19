
package org.navitrace.handler;

import jakarta.inject.Inject;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.model.Position;

public class HemisphereHandler extends BasePositionHandler {

    private int latitudeFactor;
    private int longitudeFactor;

    @Inject
    public HemisphereHandler(Config config) {
        String latitudeHemisphere = config.getString(Keys.LOCATION_LATITUDE_HEMISPHERE);
        if (latitudeHemisphere != null) {
            if (latitudeHemisphere.equalsIgnoreCase("N")) {
                latitudeFactor = 1;
            } else if (latitudeHemisphere.equalsIgnoreCase("S")) {
                latitudeFactor = -1;
            }
        }
        String longitudeHemisphere = config.getString(Keys.LOCATION_LONGITUDE_HEMISPHERE);
        if (longitudeHemisphere != null) {
            if (longitudeHemisphere.equalsIgnoreCase("E")) {
                longitudeFactor = 1;
            } else if (longitudeHemisphere.equalsIgnoreCase("W")) {
                longitudeFactor = -1;
            }
        }
    }

    @Override
    public void handlePosition(Position position, Callback callback) {
        if (latitudeFactor != 0) {
            position.setLatitude(Math.abs(position.getLatitude()) * latitudeFactor);
        }
        if (longitudeFactor != 0) {
            position.setLongitude(Math.abs(position.getLongitude()) * longitudeFactor);
        }
        callback.processed(false);
    }

}
