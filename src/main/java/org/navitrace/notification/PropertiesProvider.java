
package org.navitrace.notification;

import org.navitrace.config.Config;
import org.navitrace.config.ConfigKey;
import org.navitrace.model.ExtendedModel;

public class PropertiesProvider {

    private Config config;

    private ExtendedModel extendedModel;

    public PropertiesProvider(Config config) {
        this.config = config;
    }

    public PropertiesProvider(ExtendedModel extendedModel) {
        this.extendedModel = extendedModel;
    }

    public String getString(ConfigKey<String> key) {
        if (config != null) {
            return config.getString(key);
        } else {
            String result = extendedModel.getString(key.getKey());
            return result != null ? result : key.getDefaultValue();
        }
    }

    public int getInteger(ConfigKey<Integer> key) {
        if (config != null) {
            return config.getInteger(key);
        } else {
            Object result = extendedModel.getAttributes().get(key.getKey());
            if (result != null) {
                return result instanceof String stringResult ? Integer.parseInt(stringResult) : (Integer) result;
            } else {
                return key.getDefaultValue();
            }
        }
    }

    public Boolean getBoolean(ConfigKey<Boolean> key) {
        if (config != null) {
            if (config.hasKey(key)) {
                return config.getBoolean(key);
            } else {
                return null;
            }
        } else {
            Object result = extendedModel.getAttributes().get(key.getKey());
            if (result != null) {
                return result instanceof String stringResult ? Boolean.valueOf(stringResult) : (Boolean) result;
            } else {
                return null;
            }
        }
    }

}
