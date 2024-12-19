
package org.navitrace.reports.common;

import org.navitrace.config.Keys;
import org.navitrace.helper.model.AttributeUtil;

public class TripsConfig {

    public TripsConfig(AttributeUtil.Provider attributeProvider) {
        this(
                AttributeUtil.lookup(attributeProvider, Keys.REPORT_TRIP_MINIMAL_TRIP_DISTANCE),
                AttributeUtil.lookup(attributeProvider, Keys.REPORT_TRIP_MINIMAL_TRIP_DURATION) * 1000,
                AttributeUtil.lookup(attributeProvider, Keys.REPORT_TRIP_MINIMAL_PARKING_DURATION) * 1000,
                AttributeUtil.lookup(attributeProvider, Keys.REPORT_TRIP_MINIMAL_NO_DATA_DURATION) * 1000,
                AttributeUtil.lookup(attributeProvider, Keys.REPORT_TRIP_USE_IGNITION));
    }

    public TripsConfig(
            double minimalTripDistance, long minimalTripDuration, long minimalParkingDuration,
            long minimalNoDataDuration, boolean useIgnition) {
        this.minimalTripDistance = minimalTripDistance;
        this.minimalTripDuration = minimalTripDuration;
        this.minimalParkingDuration = minimalParkingDuration;
        this.minimalNoDataDuration = minimalNoDataDuration;
        this.useIgnition = useIgnition;
    }

    private final double minimalTripDistance;

    public double getMinimalTripDistance() {
        return minimalTripDistance;
    }

    private final long minimalTripDuration;

    public long getMinimalTripDuration() {
        return minimalTripDuration;
    }

    private final long minimalParkingDuration;

    public long getMinimalParkingDuration() {
        return minimalParkingDuration;
    }

    private final long minimalNoDataDuration;

    public long getMinimalNoDataDuration() {
        return minimalNoDataDuration;
    }

    private final boolean useIgnition;

    public boolean getUseIgnition() {
        return useIgnition;
    }

}
