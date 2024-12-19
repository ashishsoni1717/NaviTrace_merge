
package org.navitrace.broadcast;

import org.navitrace.model.BaseModel;
import org.navitrace.model.Device;
import org.navitrace.model.Event;
import org.navitrace.model.ObjectOperation;
import org.navitrace.model.Position;

public interface BroadcastInterface {

    default void updateDevice(boolean local, Device device) {
    }

    default void updatePosition(boolean local, Position position) {
    }

    default void updateEvent(boolean local, long userId, Event event) {
    }

    default void updateCommand(boolean local, long deviceId) {
    }

    default <T extends BaseModel> void invalidateObject(
            boolean local, Class<T> clazz, long id, ObjectOperation operation) throws Exception {
    }

    default <T1 extends BaseModel, T2 extends BaseModel> void invalidatePermission(
            boolean local, Class<T1> clazz1, long id1, Class<T2> clazz2, long id2, boolean link) throws Exception {
    }

}
