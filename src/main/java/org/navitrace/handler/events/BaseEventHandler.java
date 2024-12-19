
package org.navitrace.handler.events;

import org.navitrace.model.Event;
import org.navitrace.model.Position;

public abstract class BaseEventHandler {

    public interface Callback {
        void eventDetected(Event event);
    }

    /**
     * Event handlers should be processed synchronously.
     */
    public abstract void analyzePosition(Position position, Callback callback);
}
