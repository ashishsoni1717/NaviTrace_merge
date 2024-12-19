
package org.navitrace.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.navitrace.BaseProtocolDecoder;
import org.navitrace.session.DeviceSession;
import org.navitrace.NetworkMessage;
import org.navitrace.Protocol;
import org.navitrace.helper.DateBuilder;
import org.navitrace.model.CellTower;
import org.navitrace.model.Network;
import org.navitrace.model.Position;
import org.navitrace.model.WifiAccessPoint;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class SmokeyProtocolDecoder extends BaseProtocolDecoder {

    public SmokeyProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    public static final int MSG_DATE_RECORD = 0;
    public static final int MSG_DATE_RECORD_ACK = 1;

    private static void sendResponse(
            Channel channel, SocketAddress remoteAddress, ByteBuf id, int index, int report) {

        if (channel != null) {
            ByteBuf response = Unpooled.buffer();
            response.writeBytes("SM".getBytes(StandardCharsets.US_ASCII));
            response.writeByte(3); // protocol version
            response.writeByte(MSG_DATE_RECORD_ACK);
            response.writeBytes(id);
            response.writeInt(
                    (int) ChronoUnit.SECONDS.between(Instant.parse("2000-01-01T00:00:00.00Z"), Instant.now()));
            response.writeByte(index);
            response.writeByte(report - 0x200);

            short checksum = (short) 0xF5A0;
            for (int i = 0; i < response.readableBytes(); i += 2) {
                checksum ^= response.getShortLE(i);
            }
            response.writeShort(checksum);

            channel.writeAndFlush(new NetworkMessage(response, remoteAddress));
        }
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;

        buf.skipBytes(2); // header
        buf.readUnsignedByte(); // protocol version

        int type = buf.readUnsignedByte();

        ByteBuf id = buf.readSlice(8);
        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, ByteBufUtil.hexDump(id));
        if (deviceSession == null) {
            return null;
        }

        if (type == MSG_DATE_RECORD) {

            Position position = new Position(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            position.set(Position.KEY_VERSION_FW, buf.readUnsignedShort());

            int status = buf.readUnsignedShort();
            position.set(Position.KEY_STATUS, status);

            DateBuilder dateBuilder = new DateBuilder()
                    .setDate(2000, 1, 1).addSeconds(buf.readUnsignedInt());

            getLastLocation(position, dateBuilder.getDate());

            int index = buf.readUnsignedByte();
            position.set(Position.KEY_INDEX, index);

            int report = buf.readUnsignedShort();

            buf.readUnsignedShort(); // length

            position.set(Position.KEY_BATTERY, buf.readUnsignedShort());

            Network network = new Network();

            if (report != 0x0203) {

                int count = 1;
                if (report != 0x0200) {
                    count = buf.readUnsignedByte();
                }

                for (int i = 0; i < count; i++) {
                    int mcc = buf.readUnsignedShort();
                    int mnc = buf.readUnsignedShort();
                    int lac = buf.readUnsignedShort();
                    int cid = buf.readUnsignedShort();
                    if (i == 0) {
                        buf.readByte(); // timing advance
                    }
                    int rssi = buf.readByte();
                    network.addCellTower(CellTower.from(mcc, mnc, lac, cid, rssi));
                }

            }

            if (report == 0x0202 || report == 0x0203) {

                int count = buf.readUnsignedByte();

                for (int i = 0; i < count; i++) {
                    buf.readerIndex(buf.indexOf(buf.readerIndex(), buf.writerIndex(), (byte) 0) + 1); // ssid

                    String mac = String.format("%02x:%02x:%02x:%02x:%02x:%02x",
                            buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readUnsignedByte(),
                            buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readUnsignedByte());

                    network.addWifiAccessPoint(WifiAccessPoint.from(mac, buf.readByte()));
                }

            }

            position.setNetwork(network);

            sendResponse(channel, remoteAddress, id, index, report);

            return position;

        }

        return null;
    }

}
