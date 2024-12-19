
package org.navitrace.reports;

import org.navitrace.helper.model.DeviceUtil;
import org.navitrace.helper.model.PositionUtil;
import org.navitrace.model.Device;
import org.navitrace.model.Event;
import org.navitrace.reports.common.ReportUtils;
import org.navitrace.reports.model.CombinedReportItem;
import org.navitrace.storage.Storage;
import org.navitrace.storage.StorageException;
import org.navitrace.storage.query.Columns;
import org.navitrace.storage.query.Condition;
import org.navitrace.storage.query.Order;
import org.navitrace.storage.query.Request;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

public class CombinedReportProvider {

    private static final Set<String> EXCLUDE_TYPES = Set.of(Event.TYPE_DEVICE_MOVING);

    private final ReportUtils reportUtils;
    private final Storage storage;

    @Inject
    public CombinedReportProvider(ReportUtils reportUtils, Storage storage) {
        this.reportUtils = reportUtils;
        this.storage = storage;
    }

    public Collection<CombinedReportItem> getObjects(
            long userId, Collection<Long> deviceIds, Collection<Long> groupIds,
            Date from, Date to) throws StorageException {
        reportUtils.checkPeriodLimit(from, to);

        ArrayList<CombinedReportItem> result = new ArrayList<>();
        for (Device device: DeviceUtil.getAccessibleDevices(storage, userId, deviceIds, groupIds)) {
            CombinedReportItem item = new CombinedReportItem();
            item.setDeviceId(device.getId());
            var positions = PositionUtil.getPositions(storage, device.getId(), from, to);
            item.setRoute(positions.stream()
                    .map(p -> new double[] {p.getLongitude(), p.getLatitude()})
                    .collect(Collectors.toList()));
            var events = storage.getObjects(Event.class, new Request(
                    new Columns.All(),
                    new Condition.And(
                            new Condition.Equals("deviceId", device.getId()),
                            new Condition.Between("eventTime", "from", from, "to", to)),
                    new Order("eventTime")));
            item.setEvents(events.stream()
                    .filter(e -> e.getPositionId() > 0 && !EXCLUDE_TYPES.contains(e.getType()))
                    .collect(Collectors.toList()));
            var eventPositions = events.stream()
                    .map(Event::getPositionId)
                    .collect(Collectors.toSet());
            item.setPositions(positions.stream()
                    .filter(p -> eventPositions.contains(p.getId()))
                    .collect(Collectors.toList()));
            result.add(item);
        }
        return result;
    }
}
