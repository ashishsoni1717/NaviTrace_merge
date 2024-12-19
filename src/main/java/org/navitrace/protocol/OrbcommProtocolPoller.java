
package org.navitrace.protocol;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringEncoder;
import org.navitrace.BaseProtocolPoller;
import org.navitrace.Protocol;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;

import java.net.SocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class OrbcommProtocolPoller extends BaseProtocolPoller {

    private final String accessId;
    private final String password;
    private final String host;

    private Date startTime = new Date();

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public OrbcommProtocolPoller(Protocol protocol, Config config) {
        super(config.getLong(Keys.PROTOCOL_INTERVAL.withPrefix(protocol.getName())));
        accessId = config.getString(Keys.ORBCOMM_ACCESS_ID);
        password = config.getString(Keys.ORBCOMM_PASSWORD);
        host = config.getString(Keys.PROTOCOL_ADDRESS.withPrefix(protocol.getName()));
    }

    @Override
    protected void sendRequest(Channel channel, SocketAddress remoteAddress) {

        QueryStringEncoder encoder = new QueryStringEncoder("/GLGW/2/RestMessages.svc/JSON/get_return_messages/");
        encoder.addParam("access_id", accessId);
        encoder.addParam("password", password);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        encoder.addParam("start_utc", dateFormat.format(startTime));

        HttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.GET, encoder.toString(), Unpooled.buffer());
        request.headers().add(HttpHeaderNames.HOST, host);
        request.headers().add(HttpHeaderNames.CONTENT_LENGTH, 0);
        channel.writeAndFlush(request);
    }

}
