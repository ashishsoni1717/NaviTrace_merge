
package org.navitrace.geocoder;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;

public class PositionStackGeocoder extends JsonGeocoder {

    private static String formatUrl(String key) {
        return "http://api.positionstack.com/v1/reverse?access_key=" + key + "&query=%f,%f";
    }

    public PositionStackGeocoder(Client client, String key, int cacheSize, AddressFormat addressFormat) {
        super(client, formatUrl(key), cacheSize, addressFormat);
    }

    @Override
    public Address parseAddress(JsonObject json) {
        JsonArray result = json.getJsonArray("data");

        if (result != null && !result.isEmpty()) {
            JsonObject record = result.getJsonObject(0);

            Address address = new Address();

            address.setFormattedAddress(readValue(record, "label"));
            address.setHouse(readValue(record, "number"));
            address.setStreet(readValue(record, "street"));
            address.setSuburb(readValue(record, "neighbourhood"));
            address.setSettlement(readValue(record, "locality"));
            address.setState(readValue(record, "region"));
            address.setCountry(readValue(record, "country_code"));
            address.setPostcode(readValue(record, "postal_code"));

            return address;
        }

        return null;
    }

}
