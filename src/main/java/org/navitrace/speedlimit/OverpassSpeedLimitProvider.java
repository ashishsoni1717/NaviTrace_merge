
package org.navitrace.speedlimit;

import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.helper.UnitsConverter;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.AsyncInvoker;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.InvocationCallback;

public class OverpassSpeedLimitProvider implements SpeedLimitProvider {

    private final Client client;
    private final String url;

    public OverpassSpeedLimitProvider(Config config, Client client, String url) {
        int accuracy = config.getInteger(Keys.SPEED_LIMIT_ACCURACY);
        this.client = client;
        this.url = url + "?data=[out:json];way[maxspeed](around:" + accuracy + ",%f,%f);out%%20tags;";
    }

    private Double parseSpeed(String value) {
        if (value.endsWith(" mph")) {
            return UnitsConverter.knotsFromMph(Double.parseDouble(value.substring(0, value.length() - 4)));
        } else if (value.endsWith(" knots")) {
            return Double.parseDouble(value.substring(0, value.length() - 6));
        } else if (value.matches("\\d+")) {
            return UnitsConverter.knotsFromKph(Double.parseDouble(value));
        } else {
            return null;
        }
    }

    @Override
    public void getSpeedLimit(double latitude, double longitude, SpeedLimitProviderCallback callback) {
        String formattedUrl = String.format(url, latitude, longitude);
        AsyncInvoker invoker = client.target(formattedUrl).request().async();
        invoker.get(new InvocationCallback<JsonObject>() {
            @Override
            public void completed(JsonObject json) {
                JsonArray elements = json.getJsonArray("elements");
                if (!elements.isEmpty()) {
                    Double maxSpeed = parseSpeed(
                            elements.getJsonObject(0).getJsonObject("tags").getString("maxspeed"));
                    if (maxSpeed != null) {
                        callback.onSuccess(maxSpeed);
                    } else {
                        callback.onFailure(new SpeedLimitException("Parsing failed"));
                    }
                } else {
                    callback.onFailure(new SpeedLimitException("Not found"));
                }
            }

            @Override
            public void failed(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

}
