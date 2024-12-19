package org.navitrace.model;


import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Userrole extends BaseModel {

    public static final String KEY_ROLE_NAME = "role_name" ;
    public static final String KEY_DEVICE_CRUD_PERMISSION = "device_crud_perm" ;
    public static final String KEY_GROUP_CRUD_PERMISSION =  "group_crud_perm" ;
    public static final String KEY_ALERTS_CRUD_PERMISSION =  "alerts_crud_perm" ;
    public static final String KEY_GEOFENCE_CRUD_PERMISSION =  "geofence_crud_perm" ;
    public static final String KEY_DRIVERS_CRUD_PERMISSION =  "drivers_crud_perm" ;
    public static final String KEY_USERS_CRUD_PERMISSION =  "users_crud_perm" ;
    public static final String KEY_CALENDERS_CRUD_PERMISSION =  "calendars_crud_perm" ;
    public static final String KEY_DASHBOARD_CRUD_PERMISSION =  "report_dashboard_crud_perm" ;
    public static final String KEY_PANIC_CRUD_PERMISSION =  "panic_dashboard_crud_perm" ;

    private Map<String, Object> roles = new LinkedHashMap<>();
    private String rolename ;

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public Map<String, Object> getRoles() {
        return roles;
    }

    public void setRoles(Map<String, Object> roles) {
        this.roles = roles;
    }

    public void set(String key, Boolean value) {
        if (value != null) {
            roles.put(key, value);
        }
    }

    public void set(String key, Byte value) {
        if (value != null) {
            roles.put(key, value.intValue());
        }
    }

    public void set(String key, Short value) {
        if (value != null) {
            roles.put(key, value.intValue());
        }
    }

    public void set(String key, Integer value) {
        if (value != null) {
            roles.put(key, value);
        }
    }

    public void set(String key, Long value) {
        if (value != null) {
            roles.put(key, value);
        }
    }

    public void set(String key, Float value) {
        if (value != null) {
            roles.put(key, value.doubleValue());
        }
    }

    public void set(String key, Double value) {
        if (value != null) {
            roles.put(key, value);
        }
    }

    public void set(String key, String value) {
        if (value != null && !value.isEmpty()) {
            roles.put(key, value);
        }
    }

    public void add(Map.Entry<String, Object> entry) {
        if (entry != null && entry.getValue() != null) {
            roles.put(entry.getKey(), entry.getValue());
        }
    }

    public String getString(String key) {
        if (roles.containsKey(key)) {
            return (String) roles.get(key);
        } else {
            return null;
        }
    }

    public double getDouble(String key) {
        if (roles.containsKey(key)) {
            return ((Number) roles.get(key)).doubleValue();
        } else {
            return 0.0;
        }
    }

    public boolean getBoolean(String key) {
        if (roles.containsKey(key)) {
            return (Boolean) roles.get(key);
        } else {
            return false;
        }
    }

    public int getInteger(String key) {
        if (roles.containsKey(key)) {
            return ((Number) roles.get(key)).intValue();
        } else {
            return 0;
        }
    }

    public long getLong(String key) {
        if (roles.containsKey(key)) {
            return ((Number) roles.get(key)).longValue();
        } else {
            return 0;
        }
    }

    private Date createdon ;

    private Date lastupdate;

    public Date getCreatedon() {
        if (createdon != null) {
            return new Date(createdon.getTime());
        } else {
            return null;
        }
    }

//    @QueryExtended
    public void setCreatedon(Date createdOn) {
        if (createdOn != null) {
            this.createdon = new Date(createdOn.getTime());
        } else {
            this.createdon = null;
        }
    }

    public Date getLastupdate() {
        if (lastupdate != null) {
            return new Date(lastupdate.getTime());
        } else {
            return null;
        }
    }

    public void setLastupdate(Date lastupdate) {
        if (lastupdate != null) {
            this.lastupdate = new Date(lastupdate.getTime());
        } else {
            this.lastupdate = null;
        }
    }
}