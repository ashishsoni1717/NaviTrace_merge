
package org.navitrace.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.navitrace.BaseProtocolEncoder;
import org.navitrace.Protocol;
import org.navitrace.config.Keys;
import org.navitrace.helper.Checksum;
import org.navitrace.helper.DataConverter;
import org.navitrace.helper.model.AttributeUtil;
import org.navitrace.model.Command;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.TimeZone;

public class MeiligaoProtocolEncoder extends BaseProtocolEncoder {

    public MeiligaoProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    private ByteBuf encodeContent(long deviceId, int type, ByteBuf content) {

        ByteBuf buf = Unpooled.buffer();

        buf.writeByte('@');
        buf.writeByte('@');

        buf.writeShort(2 + 2 + 7 + 2 + content.readableBytes() + 2 + 2); // message length

        buf.writeBytes(DataConverter.parseHex((getUniqueId(deviceId) + "FFFFFFFFFFFFFF").substring(0, 14)));

        buf.writeShort(type);

        buf.writeBytes(content);

        buf.writeShort(Checksum.crc16(Checksum.CRC16_CCITT_FALSE, buf.nioBuffer()));

        buf.writeByte('\r');
        buf.writeByte('\n');

        return buf;
    }

    private ByteBuf encodeOutputCommand(long deviceId, int index, int value) {

        int outputCount;
        int outputType;

        String model = getDeviceModel(deviceId);

        if (model != null && Set.of("TK510", "GT08", "TK208", "TK228", "MT05").contains(model)) {
            outputCount = 5;
            outputType = MeiligaoProtocolDecoder.MSG_OUTPUT_CONTROL_1;
        } else {
            outputCount = 1;
            boolean alternative = AttributeUtil.lookup(
                    getCacheManager(), Keys.PROTOCOL_ALTERNATIVE.withPrefix(getProtocolName()), deviceId);
            outputType = alternative
                    ? MeiligaoProtocolDecoder.MSG_OUTPUT_CONTROL_1
                    : MeiligaoProtocolDecoder.MSG_OUTPUT_CONTROL_2;
        }

        ByteBuf content = Unpooled.buffer();

        for (int i = 1; i <= outputCount; i++) {
            content.writeByte(i == index ? value : 2);
        }

        return encodeContent(deviceId, outputType, content);
    }

    @Override
    protected Object encodeCommand(Command command) {

        ByteBuf content = Unpooled.buffer();

        switch (command.getType()) {
            case Command.TYPE_POSITION_SINGLE:
                return encodeContent(command.getDeviceId(), MeiligaoProtocolDecoder.MSG_TRACK_ON_DEMAND, content);
            case Command.TYPE_POSITION_PERIODIC:
                content.writeShort(command.getInteger(Command.KEY_FREQUENCY) / 10);
                return encodeContent(command.getDeviceId(), MeiligaoProtocolDecoder.MSG_TRACK_BY_INTERVAL, content);
            case Command.TYPE_OUTPUT_CONTROL:
                int index = command.getInteger(Command.KEY_INDEX) - 1;
                int value = command.getInteger(Command.KEY_DATA);
                return encodeOutputCommand(command.getDeviceId(), index, value);
            case Command.TYPE_ENGINE_STOP:
                return encodeOutputCommand(command.getDeviceId(), 1, 1);
            case Command.TYPE_ENGINE_RESUME:
                return encodeOutputCommand(command.getDeviceId(), 1, 0);
            case Command.TYPE_ALARM_GEOFENCE:
                content.writeShort(command.getInteger(Command.KEY_RADIUS));
                return encodeContent(command.getDeviceId(), MeiligaoProtocolDecoder.MSG_MOVEMENT_ALARM, content);
            case Command.TYPE_SET_TIMEZONE:
                int offset = TimeZone.getTimeZone(command.getString(Command.KEY_TIMEZONE)).getRawOffset() / 60000;
                content.writeBytes(String.valueOf(offset).getBytes(StandardCharsets.US_ASCII));
                return encodeContent(command.getDeviceId(), MeiligaoProtocolDecoder.MSG_TIME_ZONE, content);
            case Command.TYPE_REQUEST_PHOTO:
                return encodeContent(command.getDeviceId(), MeiligaoProtocolDecoder.MSG_TAKE_PHOTO, content);
            case Command.TYPE_REBOOT_DEVICE:
                return encodeContent(command.getDeviceId(), MeiligaoProtocolDecoder.MSG_REBOOT_GPS, content);
            default:
                return null;
        }
    }

}
