
package org.navitrace.geocoder;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;

public class GeocodeJsonGeocoder extends JsonGeocoder {

    private static String formatUrl(String url, String key, String language) {
        if (url == null) {
            url = "https://photon.komoot.io/reverse";
        }
        url += "?lat=%f&lon=%f";
        if (key != null) {
            url += "&key=" + key;
        }
        if (language != null) {
            url += "&lang=" + language;
        }
        return url;
    }

    public GeocodeJsonGeocoder(
            Client client, String url, String key, String language, int cacheSize, AddressFormat addressFormat) {
        super(client, formatUrl(url, key, language), cacheSize, addressFormat);
    }

    @Override
    public Address parseAddress(JsonObject json) {
        JsonArray features = json.getJsonArray("features");
        if (!features.isEmpty()) {
            Address address = new Address();
            JsonObject properties = features.getJsonObject(0).getJsonObject("properties");

            if (properties.containsKey("label")) {
                address.setFormattedAddress(properties.getString("label"));
            }
            if (properties.containsKey("housenumber")) {
                address.setHouse(properties.getString("housenumber"));
            }
            if (properties.containsKey("street")) {
                address.setStreet(properties.getString("street"));
            }
            if (properties.containsKey("city")) {
                address.setSettlement(properties.getString("city"));
            }
            if (properties.containsKey("district")) {
                address.setDistrict(properties.getString("district"));
            }
            if (properties.containsKey("state")) {
                address.setState(properties.getString("state"));
            }
            if (properties.containsKey("countrycode")) {
                address.setCountry(properties.getString("countrycode").toUpperCase());
            }
            if (properties.containsKey("postcode")) {
                address.setPostcode(properties.getString("postcode"));
            }

            return address;
        }
        return null;
    }

}
