
package org.navitrace.handler.events;

import jakarta.inject.Inject;
import org.navitrace.model.Event;
import org.navitrace.model.Maintenance;
import org.navitrace.model.Position;
import org.navitrace.session.cache.CacheManager;

public class MaintenanceEventHandler extends BaseEventHandler {

    private final CacheManager cacheManager;

    @Inject
    public MaintenanceEventHandler(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void analyzePosition(Position position, Callback callback) {
        Position lastPosition = cacheManager.getPosition(position.getDeviceId());
        if (lastPosition == null || position.getFixTime().compareTo(lastPosition.getFixTime()) < 0) {
            return;
        }

        for (Maintenance maintenance : cacheManager.getDeviceObjects(position.getDeviceId(), Maintenance.class)) {
            if (maintenance.getPeriod() != 0) {
                double oldValue = getValue(lastPosition, maintenance.getType());
                double newValue = getValue(position, maintenance.getType());
                if (oldValue != 0.0 && newValue != 0.0 && newValue >= maintenance.getStart()) {
                    if (oldValue < maintenance.getStart()
                        || (long) ((oldValue - maintenance.getStart()) / maintenance.getPeriod())
                        < (long) ((newValue - maintenance.getStart()) / maintenance.getPeriod())) {
                        Event event = new Event(Event.TYPE_MAINTENANCE, position);
                        event.setMaintenanceId(maintenance.getId());
                        event.set(maintenance.getType(), newValue);
                        callback.eventDetected(event);
                    }
                }
            }
        }
    }

    private double getValue(Position position, String type) {
        return switch (type) {
            case "serverTime" -> position.getServerTime().getTime();
            case "deviceTime" -> position.getDeviceTime().getTime();
            case "fixTime" -> position.getFixTime().getTime();
            default -> position.getDouble(type);
        };
    }

}
