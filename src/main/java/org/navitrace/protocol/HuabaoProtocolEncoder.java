
package org.navitrace.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.navitrace.BaseProtocolEncoder;
import org.navitrace.Protocol;
import org.navitrace.config.Keys;
import org.navitrace.helper.DataConverter;
import org.navitrace.helper.model.AttributeUtil;
import org.navitrace.model.Command;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HuabaoProtocolEncoder extends BaseProtocolEncoder {

    public HuabaoProtocolEncoder(Protocol protocol) {
        super(protocol);
    }

    @Override
    protected Object encodeCommand(Command command) {

        boolean alternative = AttributeUtil.lookup(
                getCacheManager(), Keys.PROTOCOL_ALTERNATIVE.withPrefix(getProtocolName()), command.getDeviceId());

        ByteBuf id = Unpooled.wrappedBuffer(
                DataConverter.parseHex(getUniqueId(command.getDeviceId())));
        try {
            ByteBuf data = Unpooled.buffer();
            byte[] time = DataConverter.parseHex(new SimpleDateFormat("yyMMddHHmmss").format(new Date()));

            switch (command.getType()) {
                case Command.TYPE_ENGINE_STOP:
                    if (alternative) {
                        data.writeByte(0x01);
                        data.writeBytes(time);
                        return HuabaoProtocolDecoder.formatMessage(
                                HuabaoProtocolDecoder.MSG_OIL_CONTROL, id, false, data);
                    } else {
                        data.writeByte(0xf0);
                        return HuabaoProtocolDecoder.formatMessage(
                                HuabaoProtocolDecoder.MSG_TERMINAL_CONTROL, id, false, data);
                    }
                case Command.TYPE_ENGINE_RESUME:
                    if (alternative) {
                        data.writeByte(0x00);
                        data.writeBytes(time);
                        return HuabaoProtocolDecoder.formatMessage(
                                HuabaoProtocolDecoder.MSG_OIL_CONTROL, id, false, data);
                    } else {
                        data.writeByte(0xf1);
                        return HuabaoProtocolDecoder.formatMessage(
                                HuabaoProtocolDecoder.MSG_TERMINAL_CONTROL, id, false, data);
                    }
                default:
                    return null;
            }
        } finally {
            id.release();
        }
    }

}
