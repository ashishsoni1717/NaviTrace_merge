package org.navitrace.misc;

import jakarta.annotation.Nullable;
import jakarta.inject.Inject;
import org.navitrace.api.signature.TokenManager;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.database.LdapProvider;
import org.navitrace.storage.Storage;
import org.navitrace.storage.StorageException;
import org.navitrace.storage.query.Columns;
import org.navitrace.storage.query.Condition;
import org.navitrace.storage.query.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.navitrace.model.User;
import org.navitrace.model.Userrole;

import java.util.Map;

public class RoleK {

    private final static Logger LOGGER = LoggerFactory.getLogger(RoleK.class);

    /** KEYS NOT IN USE, AS THESE ARE LEGACY, NOT NEEDED IN TRACKOLET****/
    public static final String KEY_ORDER_CRUD = "order" ;
    public static final String KEY_ATTRIBUTE_CRUD = "attribute" ;
    public static final String KEY_CALENDAR_CRUD = "calendar" ;
    public static final String KEY_MAINTENANCE_CRUD = "maintenance" ;
    public static final String KEY_LEG_CRUD = "leg" ;
    /********** END ************/
    /** These are the key to dig down the roles dictionary to get the value based on the key ***/
    public static final String KEY_ROLES_ALARM = "alarm" ;
    public static final String KEY_ROLES_REPORT = "report" ;
    public static final String KEY_ROLES_PERMISSION = "permission" ;
    public static final String KEY_ROLES_VIEWS = "view" ;
    /*** ALARM *****/
    public static final String ALARM_GENERAL = "general";
    public static final String ALARM_SOS = "sos";
    public static final String ALARM_VIBRATION = "vibration";
    public static final String ALARM_MOVEMENT = "movement";
    public static final String ALARM_LOW_SPEED = "low_speed";
    public static final String ALARM_OVERSPEED = "overspeed";
    public static final String ALARM_FALL_DOWN = "fall_down";
    public static final String ALARM_LOW_POWER = "low_power";
    public static final String ALARM_LOW_BATTERY = "low_battery";
    public static final String ALARM_FAULT = "fault";
    public static final String ALARM_POWER_OFF = "power_off";
    public static final String ALARM_POWER_ON = "power_on";
    public static final String ALARM_DOOR = "door";
    public static final String ALARM_LOCK = "lock";
    public static final String ALARM_UNLOCK = "unlock";
    public static final String ALARM_GEOFENCE = "geofence";
    public static final String ALARM_GEOFENCE_ENTER = "geofence_enter";
    public static final String ALARM_GEOFENCE_EXIT = "geofence_exit";
    public static final String ALARM_GPS_ANTENNA_CUT = "gps_antenna_cut";
    public static final String ALARM_ACCIDENT = "accident";
    public static final String ALARM_TOW = "tow";
    public static final String ALARM_IDLE = "idle";
    public static final String ALARM_HIGH_RPM = "high_rpm";
    public static final String ALARM_ACCELERATION = "acceleration";
    public static final String ALARM_BRAKING = "braking";
    public static final String ALARM_CORNERING = "cornering";
    public static final String ALARM_LANE_CHANGE = "lane_change";
    public static final String ALARM_FATIGUE_DRIVING = "fatigue_driving";
    public static final String ALARM_POWER_CUT = "power_cut";
    public static final String ALARM_POWER_RESTORED = "power_restored";
    public static final String ALARM_JAMMING = "jamming";
    public static final String ALARM_TEMPERATURE = "temperature";
    public static final String ALARM_PARKING = "parking";
    public static final String ALARM_BONNET = "bonnet";
    public static final String ALARM_FOOT_BRAKE = "foot_brake";
    public static final String ALARM_FUEL_LEAK = "fuel_leak";
    public static final String ALARM_TAMPERING = "tampering";
    public static final String ALARM_REMOVING = "removing";
    /*** REPORT --- KEYS ****/
    public static final String KEY_REPORT_CRUD = "all";
    public static final String KEY_REPORT_ROUTE  = "route";
    public static final String KEY_REPORT_TRIP  = "trip";
    public static final String KEY_REPORT_SUMMARY  = "summary";
    public static final String KEY_REPORT_EVENT  = "event";
    public static final String KEY_REPORT_STOP  = "stop";
    public static final String KEY_REPORT_GEOFENCE = "geofence";
    public static final String KEY_REPORT_PANIC = "panic";
    public static final String KEY_REPORT_REPORTED_VIOLATION = "reportedViolation";
    public static final String KEY_REPORT_GENERATED_VIOLATION = "generatedViolation";
    public static final String KEY_REPORT_STATISTICS = "deviceStatistics";
    public static final String KEY_REPORT_DEVICE_LOG_HISTORY = "deviceLogHistory";
    public static final String KEY_REPORT_VLTD_OEM = "vltdOem";
    /*** TAB -- KEYS **/
    public static final String KEY_AUDIT_TRAIL_CRUD = "audit_trail";
    public static final String KEY_COMMAND_CRUD = "command" ;
    public static final String KEY_DEVICE_CRUD = "device" ;
    public static final String KEY_DRIVER_CRUD = "driver" ;
    public static final String KEY_EVENT_CRUD = "event" ;
    public static final String KEY_GEOFENCE_CRUD = "geofence" ;
    public static final String KEY_GROUP_CRUD = "group" ;
    public static final String KEY_NOTIFICATION_CRUD = "notification" ;
    public static final String KEY_PERMISSION_CRUD = "control_panel";
    public static final String KEY_POSITION_CRUD = "position" ;
    public static final String KEY_ROUTE_CRUD = "route" ;
    public static final String KEY_SCHEDULE_CRUD = "schedule" ;
    public static final String KEY_SERVER_CRUD = "server" ;
    public static final String KEY_SOS_CRUD = "sos";
    public static final String KEY_STATISTIC_CRUD = "statistic" ;
    public static final String KEY_TRIP_CRUD = "trip" ;
    public static final String KEY_USERROLE_CRUD = "role" ;
    public static final String KEY_USER_CRUD = "user" ;
    public static final String KEY_VEHICLE_REG_CRUD = "vehicle_registration";
    public static final String KEY_DASHBOARD = "dashboard";
    /* Views keys */
    /* 0 means no view permission, 1 means view permission */
    public static final String KEY_DASHBOARD_OVERALL = "dashboard_overall" ;
    public static final String KEY_DASHBOARD_SUMMARY = "dashboard_vehicle_summary" ;
    public static final String KEY_VEHICLE_LIST = "vehicle_list" ;
    public static final String KEY_VEHICLE_MAP = "vehicle_map" ;
    public static final String KEY_TRAIL = "trails" ;
    public static final String KEY_TRAIL_TRIP = "trip_trail" ;
    public static final String KEY_REPORT_VEHICLE = "report_vehicle" ;
    public static final String KEY_REPORT_GROUP = "report_group" ;
    public static final String KEY_REPORT_SCHDULE = "report_schedule" ;
    public static final String KEY_PANIC = "panic_status" ;
    public static final String KEY_PANIC_SOS = "panic_sos" ;
    public static final String KEY_ENTITY = "entity" ;
    public static final String KEY_GA_GROUP = "group" ;
    public static final String KEY_AUDIT = "audit_trail" ;
    public static final String KEY_R_ROUTE = "route_list" ;
    public static final String KEY_R_CREATE = "route_assign" ;
    public static final String KEY_GEOFENCE_CONFIG = "geofence_configuration" ;
    public static final String KEY_ALARM_CONFIG = "alarm_configuration" ;
    public static final String KEY_ALARM_LOG = "alarm_log" ;
    public static final String KEY_UR_USER = "user" ;
    public static final String KEY_UR_ROLE = "role" ;
    public static final String KEY_CONTROL_PANEL = "control_panel" ;

    @Inject
    protected static Storage storage;

    public static boolean isValidCreateOperation(String roleKey, long userId) {
        try {
            User user = storage.getObject(User.class, new Request(
                    new Columns.All(),
                    new Condition.Equals("id", userId)));
            Userrole userRole = storage.getObject(Userrole.class, new Request(
                    new Columns.All(),
                    new Condition.Equals("id", user.getRoleId())));

            @SuppressWarnings("unchecked")
            Map<String, Object> subRole = (Map<String, Object>) userRole.getRoles().get(KEY_ROLES_PERMISSION);
            long value ;
            if (subRole.containsKey(roleKey)) {
                value = ((Number) subRole.get(roleKey)).intValue();
            } else {
                value = 0;
            }
            return ( value & 8) == 8;
        }catch(NullPointerException | StorageException e){
            return false ;
        }

    }

    public static boolean isValidReportRole(String roleKey, long userId) throws StorageException {
        User user = storage.getObject(User.class, new Request(
                new Columns.All(),
                new Condition.Equals("id", userId)));
        Userrole userRole = storage.getObject(Userrole.class, new Request(
                new Columns.All(),
                new Condition.Equals("id", user.getRoleId())));

        @SuppressWarnings("unchecked")
        Map<String, Object> subRole = (Map<String, Object>) userRole.getRoles().get(KEY_ROLES_REPORT);
        long value ;
        if (subRole.containsKey(roleKey)) {
            value = ((Number) subRole.get(roleKey)).intValue();
        } else {
            value = 0;
        }
        return (value & 4) == 4;
    }

    public static boolean isValidGetOperation(String roleKey, long userId)  throws StorageException {
        try {
            User user = storage.getObject(User.class, new Request(
                    new Columns.All(),
                    new Condition.Equals("id", userId)));
            Userrole userRole = storage.getObject(Userrole.class, new Request(
                    new Columns.All(),
                    new Condition.Equals("id", user.getRoleId())));

            @SuppressWarnings("unchecked")
            Map<String, Object> subRole = (Map<String, Object>) userRole.getRoles().get(KEY_ROLES_PERMISSION);
            long value ;
            if (subRole.containsKey(roleKey)) {
                value = ((Number) subRole.get(roleKey)).intValue();
            } else {
                value = 0;
            }
            return (value & 4) == 4;
        }catch(Exception  e){
            return false ;
        }
    }

    public static boolean isValidUpdateOperation(String roleKey, long userId)  throws StorageException{
        try {
            User user = storage.getObject(User.class, new Request(
                    new Columns.All(),
                    new Condition.Equals("id", userId)));
            Userrole userRole = storage.getObject(Userrole.class, new Request(
                    new Columns.All(),
                    new Condition.Equals("id", user.getRoleId())));

            @SuppressWarnings("unchecked")
            Map<String, Object> subRole = (Map<String, Object>) userRole.getRoles().get(KEY_ROLES_PERMISSION);
            long value ;
            if (subRole.containsKey(roleKey)) {
                value = ((Number) subRole.get(roleKey)).intValue();
            } else {
                value = 0;
            }
            return (value & 2) == 2;
        }catch(Exception e){
            return false ;
        }
    }

    public static boolean isValidDeleteOperation(String roleKey, long userId) {
        try {
            User user = storage.getObject(User.class, new Request(
                    new Columns.All(),
                    new Condition.Equals("id", userId)));
            Userrole userRole = storage.getObject(Userrole.class, new Request(
                    new Columns.All(),
                    new Condition.Equals("id", user.getRoleId())));

            @SuppressWarnings("unchecked")
            Map<String, Object> subRole = (Map<String, Object>) userRole.getRoles().get(KEY_ROLES_PERMISSION);
            long value ;
            if (subRole.containsKey(roleKey)) {
                value = ((Number) subRole.get(roleKey)).intValue();
            } else {
                value = 0;
            }
            return (value & 1l) == 1l;
        }catch(Exception e){
            return false ;
        }
    }

    public static boolean isUiComponentDataForUser(String roleKey, long userId){
        try{
            User user = storage.getObject(User.class, new Request(
                    new Columns.All(),
                    new Condition.Equals("id", userId)));
            Userrole userRole = storage.getObject(Userrole.class, new Request(
                    new Columns.All(),
                    new Condition.Equals("id", user.getRoleId())));

            @SuppressWarnings("unchecked")
            Map<String, Object> subRole = (Map<String, Object>) userRole.getRoles().get(KEY_ROLES_PERMISSION);
            long value ;
            if (subRole.containsKey(roleKey)) {
                value = ((Number) subRole.get(roleKey)).intValue();
            } else {
                value = 0;
            }
            return value == 1l ;
        }catch(Exception e){
            return false ;
        }
    }
}
