
package org.navitrace;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageBuilders;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttSubscribeMessage;
import org.navitrace.session.DeviceSession;

import java.net.SocketAddress;

public abstract class BaseMqttProtocolDecoder extends BaseProtocolDecoder {

    public BaseMqttProtocolDecoder(Protocol protocol) {
        super(protocol);
    }

    protected abstract Object decode(DeviceSession deviceSession, MqttPublishMessage message) throws Exception;

    @Override
    protected final Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        if (msg instanceof MqttConnectMessage message) {

            DeviceSession deviceSession = getDeviceSession(
                    channel, remoteAddress, message.payload().clientIdentifier());

            MqttConnectReturnCode returnCode = deviceSession != null
                    ? MqttConnectReturnCode.CONNECTION_ACCEPTED
                    : MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED;

            MqttMessage response = MqttMessageBuilders.connAck().returnCode(returnCode).build();

            if (channel != null) {
                channel.writeAndFlush(new NetworkMessage(response, remoteAddress));
            }

        } else if (msg instanceof MqttSubscribeMessage message) {

            MqttMessage response = MqttMessageBuilders.subAck()
                    .packetId(message.variableHeader().messageId())
                    .build();

            if (channel != null) {
                channel.writeAndFlush(new NetworkMessage(response, remoteAddress));
            }

        } else if (msg instanceof MqttPublishMessage message) {

            DeviceSession deviceSession = getDeviceSession(channel, remoteAddress);
            if (deviceSession == null) {
                return null;
            }

            Object result = decode(deviceSession, message);

            MqttMessage response = MqttMessageBuilders.pubAck()
                    .packetId(message.variableHeader().packetId())
                    .build();

            if (channel != null) {
                channel.writeAndFlush(new NetworkMessage(response, remoteAddress));
            }

            return result;

        }

        return null;
    }

}
