
package org.navitrace.notificators;

import org.navitrace.model.Event;
import org.navitrace.model.Notification;
import org.navitrace.model.Position;
import org.navitrace.model.User;
import org.navitrace.notification.MessageException;
import org.navitrace.notification.NotificationFormatter;
import org.navitrace.notification.NotificationMessage;

public abstract class Notificator {

    private final NotificationFormatter notificationFormatter;
    private final String templatePath;

    public Notificator(NotificationFormatter notificationFormatter, String templatePath) {
        this.notificationFormatter = notificationFormatter;
        this.templatePath = templatePath;
    }

    public void send(Notification notification, User user, Event event, Position position) throws MessageException {
        var message = notificationFormatter.formatMessage(notification, user, event, position, templatePath);
        send(user, message, event, position);
    }

    public void send(User user, NotificationMessage message, Event event, Position position) throws MessageException {
        throw new UnsupportedOperationException();
    }

}
