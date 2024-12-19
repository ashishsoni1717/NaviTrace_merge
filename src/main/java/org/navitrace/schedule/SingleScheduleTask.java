
package org.navitrace.schedule;

public abstract class SingleScheduleTask implements ScheduleTask {
    @Override
    public boolean multipleInstances() {
        return false;
    }
}
