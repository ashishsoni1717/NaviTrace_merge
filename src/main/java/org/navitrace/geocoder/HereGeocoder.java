
package org.navitrace.geocoder;

import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;

public class HereGeocoder extends JsonGeocoder {

    private static String formatUrl(String url, String key, String language) {
        if (url == null) {
            url = "https://revgeocode.search.hereapi.com/v1/revgeocode";
        }
        url += "?types=address&limit=1";
        url += "&at=%f,%f";
        url += "&apiKey=" + key;
        if (language != null) {
            url += "&lang=" + language;
        }
        return url;
    }

    public HereGeocoder(
            Client client, String url, String key, String language,
            int cacheSize, AddressFormat addressFormat) {
        super(client, formatUrl(url, key, language), cacheSize, addressFormat);
    }

    @Override
    public Address parseAddress(JsonObject json) {
        JsonObject result = json
                .getJsonArray("items")
                .getJsonObject(0)
                .getJsonObject("address");

        if (result != null) {
            Address address = new Address();

            if (result.containsKey("label")) {
                address.setFormattedAddress(result.getString("label"));
            }

            if (result.containsKey("houseNumber")) {
                address.setHouse(result.getString("houseNumber"));
            }
            if (result.containsKey("street")) {
                address.setStreet(result.getString("street"));
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
            if (result.containsKey("countryCode")) {
                address.setCountry(result.getString("countryCode").toUpperCase());
            }
            if (result.containsKey("postalCode")) {
                address.setPostcode(result.getString("postalCode"));
            }

            return address;
        }

        return null;
    }

}
