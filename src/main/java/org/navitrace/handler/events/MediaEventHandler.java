
package org.navitrace.handler.events;

import jakarta.inject.Inject;
import org.navitrace.model.Event;
import org.navitrace.model.Position;

import java.util.stream.Stream;

public class MediaEventHandler extends BaseEventHandler {

    @Inject
    public MediaEventHandler() {
    }

    @Override
    public void analyzePosition(Position position, Callback callback) {
        Stream.of(Position.KEY_IMAGE, Position.KEY_VIDEO, Position.KEY_AUDIO)
                .filter(position::hasAttribute)
                .map(type -> {
                    Event event = new Event(Event.TYPE_MEDIA, position);
                    event.set("media", type);
                    event.set("file", position.getString(type));
                    return event;
                })
                .forEach(callback::eventDetected);
    }

}
