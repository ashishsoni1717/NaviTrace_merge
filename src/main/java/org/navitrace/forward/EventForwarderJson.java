
package org.navitrace.forward;

import org.navitrace.config.Config;
import org.navitrace.config.Keys;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.InvocationCallback;
import jakarta.ws.rs.core.Response;

public class EventForwarderJson implements EventForwarder {

    private final String url;
    private final String header;

    private final Client client;

    public EventForwarderJson(Config config, Client client) {
        this.client = client;
        url = config.getString(Keys.EVENT_FORWARD_URL);
        header = config.getString(Keys.EVENT_FORWARD_HEADERS);
    }

    @Override
    public void forward(EventData eventData, ResultHandler resultHandler) {
        var requestBuilder = client.target(url).request();

        if (header != null && !header.isEmpty()) {
            for (String line: header.split("\\r?\\n")) {
                String[] values = line.split(":", 2);
                requestBuilder.header(values[0].trim(), values[1].trim());
            }
        }

        requestBuilder.async().post(Entity.json(eventData), new InvocationCallback<Response>() {
            @Override
            public void completed(Response response) {
                if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                    resultHandler.onResult(true, null);
                } else {
                    int code = response.getStatusInfo().getStatusCode();
                    resultHandler.onResult(false, new RuntimeException("HTTP code " + code));
                }
            }

            @Override
            public void failed(Throwable throwable) {
                resultHandler.onResult(false, throwable);
            }
        });
    }

}
