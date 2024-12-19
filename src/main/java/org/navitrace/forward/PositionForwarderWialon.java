
package org.navitrace.forward;

import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.helper.Checksum;
import org.navitrace.helper.UnitsConverter;
import org.navitrace.model.Position;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;

public class PositionForwarderWialon implements PositionForwarder {

    private final String version;

    private final DatagramSocket socket;
    private final InetAddress address;
    private final int port;

    public PositionForwarderWialon(Config config, ExecutorService executorService, String version) {
        this.version = version;
        try {
            URI url = new URI(config.getString(Keys.FORWARD_URL));
            address = InetAddress.getByName(url.getHost());
            port = url.getPort();
            socket = new DatagramSocket();
            executorService.submit(() -> {
                byte[] buffer = new byte[1024];
                while (!executorService.isShutdown()) {
                    try {
                        socket.receive(new DatagramPacket(buffer, buffer.length));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void forward(PositionData positionData, ResultHandler resultHandler) {

        DateFormat dateFormat = new SimpleDateFormat("ddMMyy;HHmmss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Position position = positionData.getPosition();
        String uniqueId = positionData.getDevice().getUniqueId();

        String payload = String.format(
                "%s;%02d%.5f;%s;%03d%.5f;%s;%d;%d;%d;NA;NA;NA;NA;;%s;NA",
                dateFormat.format(position.getFixTime()),
                (int) Math.abs(position.getLatitude()),
                Math.abs(position.getLatitude()) % 1 * 60,
                position.getLatitude() >= 0 ? "N" : "S",
                (int) Math.abs(position.getLongitude()),
                Math.abs(position.getLongitude()) % 1 * 60,
                position.getLongitude() >= 0 ? "E" : "W",
                (int) UnitsConverter.kphFromKnots(position.getSpeed()),
                (int) position.getCourse(),
                (int) position.getAltitude(),
                position.getString(Position.KEY_DRIVER_UNIQUE_ID, "NA"));

        String message;
        if (version.startsWith("2")) {
            payload += ';';
            ByteBuffer payloadBuffer = ByteBuffer.wrap(payload.getBytes(StandardCharsets.US_ASCII));
            int checksum = Checksum.crc16(Checksum.CRC16_IBM, payloadBuffer);
            message = version + ';' + uniqueId + "#D#" + payload + String.format("%04x", checksum) + "\r\n";
        } else {
            message = uniqueId + "#D#" + payload + "\r\n";
        }

        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);

        try {
            socket.send(packet);
            resultHandler.onResult(true, null);
        } catch (IOException e) {
            resultHandler.onResult(false, e);
        }
    }

}
