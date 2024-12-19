
package org.navitrace.handler.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.navitrace.NetworkMessage;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.helper.BufferUtil;
import org.navitrace.helper.NetworkUtil;
import org.navitrace.model.LogRecord;
import org.navitrace.session.ConnectionManager;

import java.nio.charset.StandardCharsets;

public class StandardLoggingHandler extends ChannelDuplexHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(StandardLoggingHandler.class);

    private final String protocol;
    private ConnectionManager connectionManager;
    private boolean decodeTextData;

    public StandardLoggingHandler(String protocol) {
        this.protocol = protocol;
    }

    @Inject
    public void setConfig(Config config) {
        decodeTextData = config.getBoolean(Keys.LOGGER_TEXT_PROTOCOL);
    }

    @Inject
    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LogRecord record = createLogRecord(ctx, msg);
        log(ctx, false, record);
        super.channelRead(ctx, msg);
        if (record != null) {
            connectionManager.updateLog(record);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        log(ctx, true, createLogRecord(ctx, msg));
        super.write(ctx, msg, promise);
    }

    private LogRecord createLogRecord(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof NetworkMessage networkMessage) {
            if (networkMessage.getMessage() instanceof ByteBuf data) {
                LogRecord record = new LogRecord(ctx.channel().localAddress(), networkMessage.getRemoteAddress());
                record.setProtocol(protocol);
                if (decodeTextData && BufferUtil.isPrintable(data, data.readableBytes())) {
                    record.setData(data.getCharSequence(
                            data.readerIndex(), data.readableBytes(), StandardCharsets.US_ASCII).toString()
                            .replace("\r", "\\r").replace("\n", "\\n"));
                } else {
                    record.setData(ByteBufUtil.hexDump(data));
                }
                return record;
            }
        }
        return null;
    }

    private void log(ChannelHandlerContext ctx, boolean downstream, LogRecord record) {
        if (record != null) {
            StringBuilder message = new StringBuilder();
            message.append("[").append(NetworkUtil.session(ctx.channel())).append(": ");
            message.append(protocol);
            message.append(downstream ? " > " : " < ");
            message.append(record.getAddress().getHostString());
            message.append("] ");
            message.append(record.getData());
            LOGGER.info(message.toString());
        }
    }

}
