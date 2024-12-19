
package org.navitrace.protocol;

import io.netty.channel.Channel;
import org.navitrace.BaseProtocolPoller;
import org.navitrace.Protocol;

import java.net.SocketAddress;

public class ArmoliProtocolPoller extends BaseProtocolPoller {

    public ArmoliProtocolPoller(Protocol protocol) {
        super(180000);
    }

    @Override
    protected void sendRequest(Channel channel, SocketAddress remoteAddress) {
        channel.writeAndFlush("[TX,];;");
    }

}
