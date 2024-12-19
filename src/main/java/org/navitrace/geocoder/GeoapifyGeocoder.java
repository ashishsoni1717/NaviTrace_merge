
package org.navitrace.geocoder;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;

public class GeoapifyGeocoder extends JsonGeocoder {

    private static String formatUrl(String key, String language) {
        String url = "https://api.geoapify.com/v1/geocode/reverse?format=json&lat=%f&lon=%f";
        if (key != null) {
            url += "&apiKey=" + key;
        }
        if (language != null) {
            url += "&lang=" + language;
        }
        return url;
    }

    public GeoapifyGeocoder(Client client, String key, String language, int cacheSize, AddressFormat addressFormat) {
        super(client, formatUrl(key, language), cacheSize, addressFormat);
    }

    @Override
    public Address parseAddress(JsonObject json) {
        JsonArray results = json.getJsonArray("results");
        if (results.size() > 0) {
            JsonObject result = results.getJsonObject(0);

            Address address = new Address();

            if (json.containsKey("formatted")) {
                address.setFormattedAddress(json.getString("formatted"));
            }

            if (result.containsKey("housenumber")) {
                address.setHouse(result.getString("housenumber"));
            }
            if (result.containsKey("street")) {
                address.setStreet(result.getString("street"));
            }
            if (result.containsKey("suburb")) {
                address.setSuburb(result.getString("suburb"));
            }
            if (result.containsKey("city")) {
                address.setSettlement(result.getString("city"));
            }
            if (result.containsKey("district")) {
                address.setDistrict(result.getString("district"));
            }
            if (result.containsKey("state")) {
                address.setState(result.getString("state"));
            }
            if (result.containsKey("country_code")) {
                address.setCountry(result.getString("country_code").toUpperCase());
            }
            if (result.containsKey("postcode")) {
                address.setPostcode(result.getString("postcode"));
            }

            return address;
        }

        return null;
    }

}
