
package org.navitrace.helper.model;

import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.model.Server;
import org.navitrace.model.User;
import org.navitrace.storage.Storage;
import org.navitrace.storage.StorageException;
import org.navitrace.storage.query.Columns;
import org.navitrace.storage.query.Order;
import org.navitrace.storage.query.Request;

import java.util.Date;
import java.util.TimeZone;

public final class UserUtil {

    private UserUtil() {
    }

    public static boolean isEmpty(Storage storage) throws StorageException {
        return storage.getObjects(User.class, new Request(
                new Columns.Include("id"),
                new Order("id", false, 1,1))).isEmpty();
    }

    public static String getDistanceUnit(Server server, User user) {
        return lookupStringAttribute(server, user, "distanceUnit", "km");
    }

    public static String getSpeedUnit(Server server, User user) {
        return lookupStringAttribute(server, user, "speedUnit", "kn");
    }

    public static String getVolumeUnit(Server server, User user) {
        return lookupStringAttribute(server, user, "volumeUnit", "ltr");
    }

    public static TimeZone getTimezone(Server server, User user) {
        String timezone = lookupStringAttribute(server, user, "timezone", null);
        return timezone != null ? TimeZone.getTimeZone(timezone) : TimeZone.getDefault();
    }

    private static String lookupStringAttribute(Server server, User user, String key, String defaultValue) {
        String preference;
        String serverPreference = server.getString(key);
        String userPreference = user.getString(key);
        if (server.getForceSettings()) {
            preference = serverPreference != null ? serverPreference : userPreference;
        } else {
            preference = userPreference != null ? userPreference : serverPreference;
        }
        return preference != null ? preference : defaultValue;
    }

    public static void setUserDefaults(User user, Config config) {
        user.setDeviceLimit(config.getInteger(Keys.USERS_DEFAULT_DEVICE_LIMIT));
        int expirationDays = config.getInteger(Keys.USERS_DEFAULT_EXPIRATION_DAYS);
        if (expirationDays > 0) {
            user.setExpirationTime(new Date(System.currentTimeMillis() + expirationDays * 86400000L));
        }
    }
}
