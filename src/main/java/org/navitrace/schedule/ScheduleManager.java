
package org.navitrace.schedule;

import com.google.inject.Injector;
import org.navitrace.LifecycleObject;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Stream;

@Singleton
public class ScheduleManager implements LifecycleObject {

    private final Injector injector;
    private final boolean secondary;
    private ScheduledExecutorService executor;

    @Inject
    public ScheduleManager(Injector injector, Config config) {
        this.injector = injector;
        secondary = config.getBoolean(Keys.BROADCAST_SECONDARY);
    }

    @Override
    public void start() {
        executor = Executors.newSingleThreadScheduledExecutor();
        Stream.of(
                TaskHealthCheck.class,
                TaskClearStatus.class,
                TaskExpirations.class,
                TaskDeleteTemporary.class,
                TaskReports.class,
                TaskDeviceInactivityCheck.class,
                TaskWebSocketKeepalive.class)
                .forEachOrdered(taskClass -> {
                    var task = injector.getInstance(taskClass);
                    if (task.multipleInstances() || !secondary) {
                        task.schedule(executor);
                    }
                });
    }

    @Override
    public void stop() {
        if (executor != null) {
            executor.shutdown();
            executor = null;
        }
    }

}
