
package org.navitrace.geocoder;

import jakarta.ws.rs.client.Client;

public class LocationIqGeocoder extends NominatimGeocoder {

    private static final String DEFAULT_URL = "https://us1.locationiq.com/v1/reverse.php";

    public LocationIqGeocoder(
            Client client, String url, String key, String language, int cacheSize, AddressFormat addressFormat) {
        super(client, url != null ? url : DEFAULT_URL, key, language, cacheSize, addressFormat);
    }

}
