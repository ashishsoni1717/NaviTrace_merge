
package org.navitrace.notificators;

import org.navitrace.model.Event;
import org.navitrace.model.Notification;
import org.navitrace.model.Position;
import org.navitrace.model.User;
import org.navitrace.notification.NotificationFormatter;
import org.navitrace.session.ConnectionManager;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public final class NotificatorWeb extends Notificator {

    private final ConnectionManager connectionManager;
    private final NotificationFormatter notificationFormatter;

    @Inject
    public NotificatorWeb(ConnectionManager connectionManager, NotificationFormatter notificationFormatter) {
        super(null, null);
        this.connectionManager = connectionManager;
        this.notificationFormatter = notificationFormatter;
    }

    @Override
    public void send(Notification notification, User user, Event event, Position position) {

        Event copy = new Event();
        copy.setId(event.getId());
        copy.setDeviceId(event.getDeviceId());
        copy.setType(event.getType());
        copy.setEventTime(event.getEventTime());
        copy.setPositionId(event.getPositionId());
        copy.setGeofenceId(event.getGeofenceId());
        copy.setMaintenanceId(event.getMaintenanceId());
        copy.getAttributes().putAll(event.getAttributes());

        var message = notificationFormatter.formatMessage(notification, user, event, position, "short");
        copy.set("message", message.getBody());

        connectionManager.updateEvent(true, user.getId(), copy);
    }

}
