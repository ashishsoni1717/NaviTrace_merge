package org.navitrace.model;


import org.navitrace.storage.StorageName;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@StorageName("tc_uiconfigs")
public class Uiconfig extends BaseModel {

    private String uiKey ;
    private Map<String, Object> configurations = new LinkedHashMap<>();
    private Date createdon ;
    private Date lastupdate;

    public String getUiKey() {
        return uiKey;
    }

    public void setUiKey(String uiKey) {
        this.uiKey = uiKey;
    }

    public Map<String, Object> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Map<String, Object> configurations) {
        this.configurations = configurations;
    }

    public void set(String key, Boolean value) {
        if (value != null) {
            configurations.put(key, value);
        }
    }

    public void set(String key, Byte value) {
        if (value != null) {
            configurations.put(key, value.intValue());
        }
    }

    public void set(String key, Short value) {
        if (value != null) {
            configurations.put(key, value.intValue());
        }
    }

    public void set(String key, Integer value) {
        if (value != null) {
            configurations.put(key, value);
        }
    }

    public void set(String key, Long value) {
        if (value != null) {
            configurations.put(key, value);
        }
    }

    public void set(String key, Float value) {
        if (value != null) {
            configurations.put(key, value.doubleValue());
        }
    }

    public void set(String key, Double value) {
        if (value != null) {
            configurations.put(key, value);
        }
    }

    public void set(String key, String value) {
        if (value != null && !value.isEmpty()) {
            configurations.put(key, value);
        }
    }

    public void add(Map.Entry<String, Object> entry) {
        if (entry != null && entry.getValue() != null) {
            configurations.put(entry.getKey(), entry.getValue());
        }
    }

    public String getString(String key) {
        if (configurations.containsKey(key)) {
            return (String) configurations.get(key);
        } else {
            return null;
        }
    }

    public double getDouble(String key) {
        if (configurations.containsKey(key)) {
            return ((Number) configurations.get(key)).doubleValue();
        } else {
            return 0.0;
        }
    }

    public boolean getBoolean(String key) {
        if (configurations.containsKey(key)) {
            return (Boolean) configurations.get(key);
        } else {
            return false;
        }
    }

    public int getInteger(String key) {
        if (configurations.containsKey(key)) {
            return ((Number) configurations.get(key)).intValue();
        } else {
            return 0;
        }
    }

    public long getLong(String key) {
        if (configurations.containsKey(key)) {
            return ((Number) configurations.get(key)).longValue();
        } else {
            return 0;
        }
    }

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