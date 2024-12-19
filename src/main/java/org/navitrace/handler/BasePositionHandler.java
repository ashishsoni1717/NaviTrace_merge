
package org.navitrace.handler;

import org.navitrace.model.Position;

public abstract class BasePositionHandler {

    public interface Callback {
        void processed(boolean filtered);
    }

    public abstract void handlePosition(Position position, Callback callback);
}
