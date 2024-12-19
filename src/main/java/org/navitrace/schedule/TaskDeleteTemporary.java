
package org.navitrace.schedule;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.navitrace.model.User;
import org.navitrace.storage.Storage;
import org.navitrace.storage.StorageException;
import org.navitrace.storage.query.Condition;
import org.navitrace.storage.query.Request;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskDeleteTemporary extends SingleScheduleTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskDeleteTemporary.class);

    private static final long CHECK_PERIOD_HOURS = 1;

    private final Storage storage;

    @Inject
    public TaskDeleteTemporary(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void schedule(ScheduledExecutorService executor) {
        executor.scheduleAtFixedRate(this, CHECK_PERIOD_HOURS, CHECK_PERIOD_HOURS, TimeUnit.HOURS);
    }

    @Override
    public void run() {
        try {
           storage.removeObject(User.class, new Request(
                    new Condition.And(
                            new Condition.Equals("temporary", true),
                            new Condition.Compare("expirationTime", "<", "time", new Date()))));
        } catch (StorageException e) {
            LOGGER.warn("Failed to delete temporary users", e);
        }
    }

}
