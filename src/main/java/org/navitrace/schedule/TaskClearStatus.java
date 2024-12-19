
package org.navitrace.schedule;

import jakarta.inject.Inject;
import org.navitrace.broadcast.BroadcastService;
import org.navitrace.helper.model.DeviceUtil;
import org.navitrace.storage.Storage;
import org.navitrace.storage.StorageException;

import java.util.concurrent.ScheduledExecutorService;

public class TaskClearStatus implements ScheduleTask {

    @Inject
    public TaskClearStatus(BroadcastService broadcastService, Storage storage) throws StorageException {
        if (broadcastService.singleInstance()) {
            DeviceUtil.resetStatus(storage);
        }
    }

    @Override
    public void schedule(ScheduledExecutorService executor) {
    }

    @Override
    public void run() {
    }

}
