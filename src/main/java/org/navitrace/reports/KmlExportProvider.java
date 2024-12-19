
package org.navitrace.reports;

import org.navitrace.helper.model.PositionUtil;
import org.navitrace.model.Device;
import org.navitrace.storage.Storage;
import org.navitrace.storage.StorageException;
import org.navitrace.storage.query.Columns;
import org.navitrace.storage.query.Condition;
import org.navitrace.storage.query.Request;

import jakarta.inject.Inject;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

public class KmlExportProvider {

    private final Storage storage;

    @Inject
    public KmlExportProvider(Storage storage) {
        this.storage = storage;
    }

    public void generate(
            OutputStream outputStream, long deviceId, Date from, Date to) throws StorageException {

        var device = storage.getObject(Device.class, new Request(
                new Columns.All(), new Condition.Equals("id", deviceId)));
        var positions = PositionUtil.getPositions(storage, deviceId, from, to);

        var dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try (PrintWriter writer = new PrintWriter(outputStream)) {
            writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.print("<kml xmlns=\"http://www.opengis.net/kml/2.2\">");
            writer.print("<Document>");
            writer.print("<name>");
            writer.print(device.getName());
            writer.print("</name>");
            writer.print("<Placemark>");
            writer.print("<name>");
            writer.print(dateFormat.format(from));
            writer.print(" - ");
            writer.print(dateFormat.format(to));
            writer.print("</name>");
            writer.print("<LineString>");
            writer.print("<extrude>1</extrude>");
            writer.print("<tessellate>1</tessellate>");
            writer.print("<altitudeMode>absolute</altitudeMode>");
            writer.print("<coordinates>");
            writer.print(positions.stream()
                    .map((p -> String.format("%f,%f,%f", p.getLongitude(), p.getLatitude(), p.getAltitude())))
                    .collect(Collectors.joining(" ")));
            writer.print("</coordinates>");
            writer.print("</LineString>");
            writer.print("</Placemark>");
            writer.print("</Document>");
            writer.print("</kml>");
        }
    }

}
