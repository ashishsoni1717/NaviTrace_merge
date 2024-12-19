
package org.navitrace.geolocation;

import jakarta.ws.rs.client.Client;

public class GoogleGeolocationProvider extends UniversalGeolocationProvider {

    private static final String URL = "https://www.googleapis.com/geolocation/v1/geolocate";

    public GoogleGeolocationProvider(Client client, String key) {
        super(client, URL, key);
    }

}
