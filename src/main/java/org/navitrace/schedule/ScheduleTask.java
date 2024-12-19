
package org.navitrace.schedule;

import java.util.concurrent.ScheduledExecutorService;

public interface ScheduleTask extends Runnable {

    default boolean multipleInstances() {
        return true;
    }

    void schedule(ScheduledExecutorService executor);
}
