
package org.navitrace.handler;

import jakarta.inject.Inject;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.model.Position;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TimeHandler extends BasePositionHandler {

    private final boolean useServerTime;
    private final Set<String> protocols;

    @Inject
    public TimeHandler(Config config) {
        useServerTime = config.getString(Keys.TIME_OVERRIDE).equalsIgnoreCase("serverTime");
        String protocolList = config.getString(Keys.TIME_PROTOCOLS);
        if (protocolList != null) {
            protocols = new HashSet<>(Arrays.asList(protocolList.split("[, ]")));
        } else {
            protocols = null;
        }
    }

    @Override
    public void handlePosition(Position position, Callback callback) {

        if (protocols == null || protocols.contains(position.getProtocol())) {
            if (useServerTime) {
                position.setDeviceTime(position.getServerTime());
                position.setFixTime(position.getServerTime());
            } else {
                position.setFixTime(position.getDeviceTime());
            }
        }
        callback.processed(false);
    }

}
