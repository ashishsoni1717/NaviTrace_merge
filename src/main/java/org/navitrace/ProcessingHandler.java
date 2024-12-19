
package org.navitrace;

import com.google.inject.Injector;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.navitrace.config.Config;
import org.navitrace.database.BufferingManager;
import org.navitrace.database.NotificationManager;
import org.navitrace.handler.BasePositionHandler;
import org.navitrace.handler.ComputedAttributesHandler;
import org.navitrace.handler.CopyAttributesHandler;
import org.navitrace.handler.DatabaseHandler;
import org.navitrace.handler.DistanceHandler;
import org.navitrace.handler.DriverHandler;
import org.navitrace.handler.EngineHoursHandler;
import org.navitrace.handler.FilterHandler;
import org.navitrace.handler.GeocoderHandler;
import org.navitrace.handler.GeofenceHandler;
import org.navitrace.handler.GeolocationHandler;
import org.navitrace.handler.HemisphereHandler;
import org.navitrace.handler.MotionHandler;
import org.navitrace.handler.OutdatedHandler;
import org.navitrace.handler.PositionForwardingHandler;
import org.navitrace.handler.PostProcessHandler;
import org.navitrace.handler.SpeedLimitHandler;
import org.navitrace.handler.TimeHandler;
import org.navitrace.handler.events.AlarmEventHandler;
import org.navitrace.handler.events.BaseEventHandler;
import org.navitrace.handler.events.BehaviorEventHandler;
import org.navitrace.handler.events.CommandResultEventHandler;
import org.navitrace.handler.events.DriverEventHandler;
import org.navitrace.handler.events.FuelEventHandler;
import org.navitrace.handler.events.GeofenceEventHandler;
import org.navitrace.handler.events.IgnitionEventHandler;
import org.navitrace.handler.events.MaintenanceEventHandler;
import org.navitrace.handler.events.MediaEventHandler;
import org.navitrace.handler.events.MotionEventHandler;
import org.navitrace.handler.events.OverspeedEventHandler;
import org.navitrace.handler.network.AcknowledgementHandler;
import org.navitrace.helper.PositionLogger;
import org.navitrace.model.Position;
import org.navitrace.session.cache.CacheManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Stream;

@Singleton
@ChannelHandler.Sharable
public class ProcessingHandler extends ChannelInboundHandlerAdapter implements BufferingManager.Callback {

    private final CacheManager cacheManager;
    private final NotificationManager notificationManager;
    private final PositionLogger positionLogger;
    private final BufferingManager bufferingManager;
    private final List<BasePositionHandler> positionHandlers;
    private final List<BaseEventHandler> eventHandlers;
    private final PostProcessHandler postProcessHandler;

    private final Map<Long, Queue<Position>> queues = new HashMap<>();

    private synchronized Queue<Position> getQueue(long deviceId) {
        return queues.computeIfAbsent(deviceId, k -> new LinkedList<>());
    }

    @Inject
    public ProcessingHandler(
            Injector injector, Config config,
            CacheManager cacheManager, NotificationManager notificationManager, PositionLogger positionLogger) {
        this.cacheManager = cacheManager;
        this.notificationManager = notificationManager;
        this.positionLogger = positionLogger;
        bufferingManager = new BufferingManager(config, this);

        positionHandlers = Stream.of(
                ComputedAttributesHandler.Early.class,
                OutdatedHandler.class,
                TimeHandler.class,
                GeolocationHandler.class,
                HemisphereHandler.class,
                DistanceHandler.class,
                FilterHandler.class,
                GeofenceHandler.class,
                GeocoderHandler.class,
                SpeedLimitHandler.class,
                MotionHandler.class,
                ComputedAttributesHandler.Late.class,
                EngineHoursHandler.class,
                DriverHandler.class,
                CopyAttributesHandler.class,
                PositionForwardingHandler.class,
                DatabaseHandler.class)
                .map((clazz) -> (BasePositionHandler) injector.getInstance(clazz))
                .filter(Objects::nonNull)
                .toList();

        eventHandlers = Stream.of(
                MediaEventHandler.class,
                CommandResultEventHandler.class,
                OverspeedEventHandler.class,
                BehaviorEventHandler.class,
                FuelEventHandler.class,
                MotionEventHandler.class,
                GeofenceEventHandler.class,
                AlarmEventHandler.class,
                IgnitionEventHandler.class,
                MaintenanceEventHandler.class,
                DriverEventHandler.class)
                .map((clazz) -> (BaseEventHandler) injector.getInstance(clazz))
                .filter(Objects::nonNull)
                .toList();

        postProcessHandler = injector.getInstance(PostProcessHandler.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Position position) {
            bufferingManager.accept(ctx, position);
        } else {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void onReleased(ChannelHandlerContext context, Position position) {
        Queue<Position> queue = getQueue(position.getDeviceId());
        boolean queued;
        synchronized (queue) {
            queued = !queue.isEmpty();
            queue.offer(position);
        }
        if (!queued) {
            try {
                cacheManager.addDevice(position.getDeviceId(), position.getDeviceId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            processPositionHandlers(context, position);
        }
    }

    private void processPositionHandlers(ChannelHandlerContext ctx, Position position) {
        var iterator = positionHandlers.iterator();
        iterator.next().handlePosition(position, new BasePositionHandler.Callback() {
            @Override
            public void processed(boolean filtered) {
                if (!filtered) {
                    if (iterator.hasNext()) {
                        iterator.next().handlePosition(position, this);
                    } else {
                        processEventHandlers(ctx, position);
                    }
                } else {
                    finishedProcessing(ctx, position, true);
                }
            }
        });
    }

    private void processEventHandlers(ChannelHandlerContext ctx, Position position) {
        eventHandlers.forEach(handler -> handler.analyzePosition(
                position, (event) -> notificationManager.updateEvents(Map.of(event, position))));
        finishedProcessing(ctx, position, false);
    }

    private void finishedProcessing(ChannelHandlerContext ctx, Position position, boolean filtered) {
        if (!filtered) {
            postProcessHandler.handlePosition(position, ignore -> {
                positionLogger.log(ctx, position);
                ctx.writeAndFlush(new AcknowledgementHandler.EventHandled(position));
                processNextPosition(ctx, position.getDeviceId());
            });
        } else {
            ctx.writeAndFlush(new AcknowledgementHandler.EventHandled(position));
            processNextPosition(ctx, position.getDeviceId());
        }
    }

    private void processNextPosition(ChannelHandlerContext ctx, long deviceId) {
        Queue<Position> queue = getQueue(deviceId);
        Position nextPosition;
        synchronized (queue) {
            queue.poll(); // remove current position
            nextPosition = queue.peek();
        }
        if (nextPosition != null) {
            processPositionHandlers(ctx, nextPosition);
        } else {
            cacheManager.removeDevice(deviceId, deviceId);
        }
    }

}
