
package org.navitrace.handler.events;

import jakarta.inject.Inject;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.model.Event;
import org.navitrace.model.Position;
import org.navitrace.session.cache.CacheManager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AlarmEventHandler extends BaseEventHandler {

    private final CacheManager cacheManager;
    private final boolean ignoreDuplicates;

    @Inject
    public AlarmEventHandler(Config config, CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        ignoreDuplicates = config.getBoolean(Keys.EVENT_IGNORE_DUPLICATE_ALERTS);
    }

    @Override
    public void analyzePosition(Position position, Callback callback) {
        String alarmString = position.getString(Position.KEY_ALARM);
        if (alarmString != null) {
            Set<String> alarms = new HashSet<>(Arrays.asList(alarmString.split(",")));
            if (ignoreDuplicates) {
                Position lastPosition = cacheManager.getPosition(position.getDeviceId());
                if (lastPosition != null) {
                    String lastAlarmString = lastPosition.getString(Position.KEY_ALARM);
                    if (lastAlarmString != null) {
                        Set<String> lastAlarms = new HashSet<>(Arrays.asList(lastAlarmString.split(",")));
                        alarms.removeAll(lastAlarms);
                    }
                }
            }
            for (String alarm : alarms) {
                Event event = new Event(Event.TYPE_ALARM, position);
                event.set(Position.KEY_ALARM, alarm);
                callback.eventDetected(event);
            }
        }
    }

}
