
package org.navitrace;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.navitrace.config.Keys;
import org.navitrace.database.CommandsManager;
import org.navitrace.database.MediaManager;
import org.navitrace.database.StatisticsManager;
import org.navitrace.helper.UnitsConverter;
import org.navitrace.helper.model.AttributeUtil;
import org.navitrace.model.Command;
import org.navitrace.model.Device;
import org.navitrace.model.Position;
import org.navitrace.session.ConnectionManager;
import org.navitrace.session.DeviceSession;
import org.navitrace.session.cache.CacheManager;

import jakarta.inject.Inject;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

public abstract class BaseProtocolDecoder extends ExtendedObjectDecoder {

    private static final String PROTOCOL_UNKNOWN = "unknown";

    private final Protocol protocol;

    private CacheManager cacheManager;
    private ConnectionManager connectionManager;
    private StatisticsManager statisticsManager;
    private MediaManager mediaManager;
    private CommandsManager commandsManager;

    private String modelOverride;

    public BaseProtocolDecoder(Protocol protocol) {
        this.protocol = protocol;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    @Inject
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Inject
    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Inject
    public void setStatisticsManager(StatisticsManager statisticsManager) {
        this.statisticsManager = statisticsManager;
    }

    @Inject
    public void setMediaManager(MediaManager mediaManager) {
        this.mediaManager = mediaManager;
    }

    @Inject
    public void setCommandsManager(CommandsManager commandsManager) {
        this.commandsManager = commandsManager;
    }

    public CommandsManager getCommandsManager() {
        return commandsManager;
    }

    public String writeMediaFile(String uniqueId, ByteBuf buf, String extension) {
        return mediaManager.writeFile(uniqueId, buf, extension);
    }

    public String getProtocolName() {
        return protocol != null ? protocol.getName() : PROTOCOL_UNKNOWN;
    }

    public String getServer(Channel channel, char delimiter) {
        String server = getConfig().getString(Keys.PROTOCOL_SERVER.withPrefix(getProtocolName()));
        if (server == null && channel != null) {
            InetSocketAddress address = (InetSocketAddress) channel.localAddress();
            server = address.getAddress().getHostAddress() + ":" + address.getPort();
        }
        return server != null ? server.replace(':', delimiter) : null;
    }

    protected double convertSpeed(double value, String defaultUnits) {
        return switch (getConfig().getString(getProtocolName() + ".speed", defaultUnits)) {
            case "kmh" -> UnitsConverter.knotsFromKph(value);
            case "mps" -> UnitsConverter.knotsFromMps(value);
            case "mph" -> UnitsConverter.knotsFromMph(value);
            default -> value;
        };
    }

    protected TimeZone getTimeZone(long deviceId) {
        return getTimeZone(deviceId, "UTC");
    }

    protected TimeZone getTimeZone(long deviceId, String defaultTimeZone) {
        String timeZoneName = AttributeUtil.lookup(cacheManager, Keys.DECODER_TIMEZONE, deviceId);
        if (timeZoneName != null) {
            return TimeZone.getTimeZone(timeZoneName);
        } else if (defaultTimeZone != null) {
            return TimeZone.getTimeZone(defaultTimeZone);
        }
        return null;
    }

    public DeviceSession getDeviceSession(Channel channel, SocketAddress remoteAddress, String... uniqueIds) {
        try {
            return connectionManager.getDeviceSession(protocol, channel, remoteAddress, uniqueIds);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setModelOverride(String modelOverride) {
        this.modelOverride = modelOverride;
    }

    public String getDeviceModel(DeviceSession deviceSession) {
        return modelOverride != null ? modelOverride : deviceSession.getModel();
    }

    public void getLastLocation(Position position, Date deviceTime) {
        if (position.getDeviceId() != 0) {
            position.setOutdated(true);
            if (deviceTime != null) {
                position.setDeviceTime(deviceTime);
            }
        }
    }

    @Override
    protected void onMessageEvent(
            Channel channel, SocketAddress remoteAddress, Object originalMessage, Object decodedMessage) {
        if (statisticsManager != null) {
            statisticsManager.registerMessageReceived();
        }
        Set<Long> deviceIds = new HashSet<>();
        if (decodedMessage != null) {
            if (decodedMessage instanceof Position position) {
                deviceIds.add(position.getDeviceId());
            } else if (decodedMessage instanceof Collection) {
                Collection<Position> positions = (Collection) decodedMessage;
                for (Position position : positions) {
                    deviceIds.add(position.getDeviceId());
                }
            }
        }
        if (deviceIds.isEmpty()) {
            DeviceSession deviceSession = getDeviceSession(channel, remoteAddress);
            if (deviceSession != null) {
                deviceIds.add(deviceSession.getDeviceId());
            }
        }
        for (long deviceId : deviceIds) {
            connectionManager.updateDevice(deviceId, Device.STATUS_ONLINE, new Date());
            sendQueuedCommands(channel, remoteAddress, deviceId);
        }
    }

    protected void sendQueuedCommands(Channel channel, SocketAddress remoteAddress, long deviceId) {
        for (Command command : commandsManager.readQueuedCommands(deviceId)) {
            protocol.sendDataCommand(channel, remoteAddress, command);
        }
    }

    @Override
    protected Object handleEmptyMessage(Channel channel, SocketAddress remoteAddress, Object msg) {
        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress);
        if (getConfig().getBoolean(Keys.DATABASE_SAVE_EMPTY) && deviceSession != null) {
            Position position = new Position(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());
            getLastLocation(position, null);
            return position;
        } else {
            return null;
        }
    }

}
