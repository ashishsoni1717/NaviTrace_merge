
package org.navitrace.reports;

import org.navitrace.helper.DateUtil;
import org.navitrace.helper.model.PositionUtil;
import org.navitrace.model.Position;
import org.navitrace.storage.Storage;
import org.navitrace.storage.StorageException;

import jakarta.inject.Inject;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CsvExportProvider {

    private final Storage storage;

    @Inject
    public CsvExportProvider(Storage storage) {
        this.storage = storage;
    }

    public void generate(
            OutputStream outputStream, long deviceId, Date from, Date to) throws StorageException {

        var positions = PositionUtil.getPositions(storage, deviceId, from, to);

        var attributes = positions.stream()
                .flatMap((position -> position.getAttributes().keySet().stream()))
                .collect(Collectors.toUnmodifiableSet());

        var properties = new LinkedHashMap<String, Function<Position, Object>>();
        properties.put("id", Position::getId);
        properties.put("deviceId", Position::getDeviceId);
        properties.put("protocol", Position::getProtocol);
        properties.put("serverTime", position -> DateUtil.formatDate(position.getServerTime()));
        properties.put("deviceTime", position -> DateUtil.formatDate(position.getDeviceTime()));
        properties.put("fixTime", position -> DateUtil.formatDate(position.getFixTime()));
        properties.put("valid", Position::getValid);
        properties.put("latitude", Position::getLatitude);
        properties.put("longitude", Position::getLongitude);
        properties.put("altitude", Position::getAltitude);
        properties.put("speed", Position::getSpeed);
        properties.put("course", Position::getCourse);
        properties.put("address", Position::getAddress);
        properties.put("accuracy", Position::getAccuracy);
        attributes.forEach(key -> properties.put(key, position -> position.getAttributes().get(key)));

        try (PrintWriter writer = new PrintWriter(outputStream)) {
            writer.println(String.join(",", properties.keySet()));
            positions.forEach(position -> writer.println(properties.values().stream()
                    .map(f -> Objects.toString(f.apply(position), ""))
                    .collect(Collectors.joining(","))));
        }
    }

}
