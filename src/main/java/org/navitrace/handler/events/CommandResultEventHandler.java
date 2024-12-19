
package org.navitrace.handler.events;

import jakarta.inject.Inject;
import org.navitrace.model.Event;
import org.navitrace.model.Position;

public class CommandResultEventHandler extends BaseEventHandler {

    @Inject
    public CommandResultEventHandler() {
    }

    @Override
    public void analyzePosition(Position position, Callback callback) {
        Object commandResult = position.getAttributes().get(Position.KEY_RESULT);
        if (commandResult != null) {
            Event event = new Event(Event.TYPE_COMMAND_RESULT, position);
            event.set(Position.KEY_RESULT, (String) commandResult);
            callback.eventDetected(event);
        }
    }

}
