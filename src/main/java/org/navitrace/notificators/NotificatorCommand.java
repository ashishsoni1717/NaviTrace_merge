
package org.navitrace.notificators;

import org.navitrace.database.CommandsManager;
import org.navitrace.model.Command;
import org.navitrace.model.Event;
import org.navitrace.model.Notification;
import org.navitrace.model.Position;
import org.navitrace.model.User;
import org.navitrace.notification.MessageException;
import org.navitrace.storage.Storage;
import org.navitrace.storage.query.Columns;
import org.navitrace.storage.query.Condition;
import org.navitrace.storage.query.Request;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class NotificatorCommand extends Notificator {

    private final Storage storage;
    private final CommandsManager commandsManager;

    @Inject
    public NotificatorCommand(Storage storage, CommandsManager commandsManager) {
        super(null, null);
        this.storage = storage;
        this.commandsManager = commandsManager;
    }

    @Override
    public void send(Notification notification, User user, Event event, Position position) throws MessageException {

        if (notification == null || notification.getCommandId() <= 0) {
            throw new MessageException("Saved command not provided");
        }

        try {
            Command command = storage.getObject(Command.class, new Request(
                    new Columns.All(), new Condition.Equals("id", notification.getCommandId())));
            command.setDeviceId(event.getDeviceId());
            commandsManager.sendCommand(command);
        } catch (Exception e) {
            throw new MessageException(e);
        }
    }

}
