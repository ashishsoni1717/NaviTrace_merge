
package org.navitrace.handler;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.navitrace.helper.model.PositionUtil;
import org.navitrace.model.Device;
import org.navitrace.model.Position;
import org.navitrace.session.ConnectionManager;
import org.navitrace.session.cache.CacheManager;
import org.navitrace.storage.Storage;
import org.navitrace.storage.StorageException;
import org.navitrace.storage.query.Columns;
import org.navitrace.storage.query.Condition;
import org.navitrace.storage.query.Request;

public class PostProcessHandler extends BasePositionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostProcessHandler.class);

    private final CacheManager cacheManager;
    private final Storage storage;
    private final ConnectionManager connectionManager;

    @Inject
    public PostProcessHandler(CacheManager cacheManager, Storage storage, ConnectionManager connectionManager) {
        this.cacheManager = cacheManager;
        this.storage = storage;
        this.connectionManager = connectionManager;
    }

    @Override
    public void handlePosition(Position position, Callback callback) {
        try {
            if (PositionUtil.isLatest(cacheManager, position)) {
                Device updatedDevice = new Device();
                updatedDevice.setId(position.getDeviceId());
                updatedDevice.setPositionId(position.getId());
                storage.updateObject(updatedDevice, new Request(
                        new Columns.Include("positionId"),
                        new Condition.Equals("id", updatedDevice.getId())));

                cacheManager.updatePosition(position);
                connectionManager.updatePosition(true, position);
            }
        } catch (StorageException error) {
            LOGGER.warn("Failed to update device", error);
        }
        callback.processed(false);
    }

}
