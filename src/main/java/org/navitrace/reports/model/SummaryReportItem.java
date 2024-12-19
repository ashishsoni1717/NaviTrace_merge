
package org.navitrace.reports.model;

public class SummaryReportItem extends BaseReportItem {

    public long getEngineHours() {
        return endHours - startHours;
    }

    private long startHours; // milliseconds

    public long getStartHours() {
        return startHours;
    }

    public void setStartHours(long startHours) {
        this.startHours = startHours;
    }

    private long endHours; // milliseconds

    public long getEndHours() {
        return endHours;
    }

    public void setEndHours(long endHours) {
        this.endHours = endHours;
    }

}
