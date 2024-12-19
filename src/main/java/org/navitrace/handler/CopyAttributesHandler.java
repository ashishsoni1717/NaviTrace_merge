
package org.navitrace.handler;

import jakarta.inject.Inject;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.helper.model.AttributeUtil;
import org.navitrace.model.Position;
import org.navitrace.session.cache.CacheManager;

public class CopyAttributesHandler extends BasePositionHandler {

    private final CacheManager cacheManager;

    @Inject
    public CopyAttributesHandler(Config config, CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void handlePosition(Position position, Callback callback) {
        String attributesString = AttributeUtil.lookup(
                cacheManager, Keys.PROCESSING_COPY_ATTRIBUTES, position.getDeviceId());
        Position last = cacheManager.getPosition(position.getDeviceId());
        if (last != null && attributesString != null) {
            for (String attribute : attributesString.split("[ ,]")) {
                if (last.hasAttribute(attribute) && !position.hasAttribute(attribute)) {
                    position.getAttributes().put(attribute, last.getAttributes().get(attribute));
                }
            }
        }
        callback.processed(false);
    }

}
