
package org.navitrace.reports;

import jakarta.inject.Inject;
import org.jxls.util.JxlsHelper;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.helper.model.PositionUtil;
import org.navitrace.model.Device;
import org.navitrace.model.Message;
import org.navitrace.model.User;
import org.navitrace.reports.common.ReportUtils;
import org.navitrace.reports.model.DeviceReportItem;
import org.navitrace.storage.Storage;
import org.navitrace.storage.StorageException;
import org.navitrace.storage.query.Columns;
import org.navitrace.storage.query.Condition;
import org.navitrace.storage.query.Request;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Collectors;

public class DevicesReportProvider {

    private final Config config;
    private final ReportUtils reportUtils;
    private final Storage storage;

    @Inject
    public DevicesReportProvider(Config config, ReportUtils reportUtils, Storage storage) {
        this.config = config;
        this.reportUtils = reportUtils;
        this.storage = storage;
    }

    public Collection<DeviceReportItem> getObjects(long userId) throws StorageException {

        var positions = PositionUtil.getLatestPositions(storage, userId).stream()
                .collect(Collectors.toMap(Message::getDeviceId, p -> p));

        return storage.getObjects(Device.class, new Request(
                new Columns.All(),
                new Condition.Permission(User.class, userId, Device.class))).stream()
                .map(device -> new DeviceReportItem(device, positions.get(device.getId())))
                .collect(Collectors.toUnmodifiableList());
    }

    public void getExcel(OutputStream outputStream, long userId) throws StorageException, IOException {

        File file = Paths.get(config.getString(Keys.TEMPLATES_ROOT), "export", "devices.xlsx").toFile();
        try (InputStream inputStream = new FileInputStream(file)) {
            var context = reportUtils.initializeContext(userId);
            context.putVar("items", getObjects(userId));
            JxlsHelper.getInstance().setUseFastFormulaProcessor(false)
                    .processTemplate(inputStream, outputStream, context);
        }
    }
}
