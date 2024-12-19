
package org.navitrace.protocol;

import com.google.inject.Injector;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.navitrace.BaseProtocolDecoder;
import org.navitrace.Protocol;

import jakarta.inject.Inject;
import java.net.SocketAddress;

public class ArnaviProtocolDecoder extends BaseProtocolDecoder {

    private final ArnaviTextProtocolDecoder textProtocolDecoder;
    private final ArnaviBinaryProtocolDecoder binaryProtocolDecoder;

    public ArnaviProtocolDecoder(Protocol protocol) {
        super(protocol);
        textProtocolDecoder = new ArnaviTextProtocolDecoder(protocol);
        binaryProtocolDecoder = new ArnaviBinaryProtocolDecoder(protocol);
    }

    @Inject
    public void setInjector(Injector injector) {
        injector.injectMembers(textProtocolDecoder);
        injector.injectMembers(binaryProtocolDecoder);
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;

        if (buf.getByte(buf.readerIndex()) == '$') {
            return textProtocolDecoder.decode(channel, remoteAddress, msg);
        } else {
            return binaryProtocolDecoder.decode(channel, remoteAddress, msg);
        }
    }

}
