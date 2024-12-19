
package org.navitrace.notificators;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.model.Event;
import org.navitrace.model.Position;
import org.navitrace.model.User;
import org.navitrace.notification.NotificationFormatter;
import org.navitrace.notification.NotificationMessage;

@Singleton
public class NotificatorPushover extends Notificator {

    private final Client client;

    private final String url;
    private final String token;
    private final String user;

    public static class Message {
        @JsonProperty("token")
        private String token;
        @JsonProperty("user")
        private String user;
        @JsonProperty("device")
        private String device;
        @JsonProperty("title")
        private String title;
        @JsonProperty("message")
        private String message;
    }

    @Inject
    public NotificatorPushover(Config config, NotificationFormatter notificationFormatter, Client client) {
        super(notificationFormatter, "short");
        this.client = client;
        url = "https://api.pushover.net/1/messages.json";
        token = config.getString(Keys.NOTIFICATOR_PUSHOVER_TOKEN);
        user = config.getString(Keys.NOTIFICATOR_PUSHOVER_USER);
    }

    @Override
    public void send(User user, NotificationMessage shortMessage, Event event, Position position) {

        Message message = new Message();
        message.token = token;

        message.user = user.getString("pushoverUserKey");
        if (message.user == null) {
            message.user = this.user;
        }

        if (user.hasAttribute("pushoverDeviceNames")) {
            message.device = user.getString("pushoverDeviceNames").replaceAll(" *, *", ",");
        }

        message.title = shortMessage.getSubject();
        message.message = shortMessage.getBody();

        client.target(url).request().post(Entity.json(message)).close();
    }

}
