
package org.navitrace.geocoder;

/*
 * API documentation: https://adresse.data.gouv.fr/api
 */

import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;

public class BanGeocoder extends GeocodeJsonGeocoder {

    public BanGeocoder(Client client, int cacheSize, AddressFormat addressFormat) {
        super(client, "https://api-adresse.data.gouv.fr/reverse/", null, null, cacheSize, addressFormat);
    }

    @Override
    public Address parseAddress(JsonObject json) {
        Address geodecoded = super.parseAddress(json);
        if (geodecoded != null) {
            geodecoded.setCountry("FR");

            return geodecoded;
        }

        return null;
    }

}
