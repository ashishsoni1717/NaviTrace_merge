
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
public class NotificatorTelegram extends Notificator {

    private final Client client;

    private final String urlSendText;
    private final String urlSendLocation;
    private final String chatId;
    private final boolean sendLocation;

    public static class TextMessage {
        @JsonProperty("chat_id")
        private String chatId;
        @JsonProperty("text")
        private String text;
        @JsonProperty("parse_mode")
        private String parseMode = "html";
    }

    public static class LocationMessage {
        @JsonProperty("chat_id")
        private String chatId;
        @JsonProperty("latitude")
        private double latitude;
        @JsonProperty("longitude")
        private double longitude;
        @JsonProperty("horizontal_accuracy")
        private double accuracy;
        @JsonProperty("bearing")
        private int bearing;
    }

    @Inject
    public NotificatorTelegram(Config config, NotificationFormatter notificationFormatter, Client client) {
        super(notificationFormatter, "short");
        this.client = client;
        urlSendText = String.format(
                "https://api.telegram.org/bot%s/sendMessage", config.getString(Keys.NOTIFICATOR_TELEGRAM_KEY));
        urlSendLocation = String.format(
                "https://api.telegram.org/bot%s/sendLocation", config.getString(Keys.NOTIFICATOR_TELEGRAM_KEY));
        chatId = config.getString(Keys.NOTIFICATOR_TELEGRAM_CHAT_ID);
        sendLocation = config.getBoolean(Keys.NOTIFICATOR_TELEGRAM_SEND_LOCATION);
    }

    private LocationMessage createLocationMessage(String messageChatId, Position position) {
        LocationMessage locationMessage = new LocationMessage();
        locationMessage.chatId = messageChatId;
        locationMessage.latitude = position.getLatitude();
        locationMessage.longitude = position.getLongitude();
        locationMessage.bearing = (int) Math.ceil(position.getCourse());
        locationMessage.accuracy = position.getAccuracy();
        return locationMessage;
    }

    @Override
    public void send(User user, NotificationMessage shortMessage, Event event, Position position) {

        TextMessage message = new TextMessage();
        message.chatId = user.getString("telegramChatId");
        if (message.chatId == null) {
            message.chatId = chatId;
        }
        message.text = shortMessage.getBody();
        client.target(urlSendText).request().post(Entity.json(message)).close();
        if (sendLocation && position != null) {
            client.target(urlSendLocation).request().post(
                    Entity.json(createLocationMessage(message.chatId, position))).close();
        }
    }

}
