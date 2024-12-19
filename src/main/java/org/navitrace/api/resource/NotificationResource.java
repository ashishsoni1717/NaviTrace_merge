
package org.navitrace.api.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.navitrace.api.ExtendedObjectResource;
import org.navitrace.model.Event;
import org.navitrace.model.Notification;
import org.navitrace.model.Typed;
import org.navitrace.model.User;
import org.navitrace.notification.MessageException;
import org.navitrace.notification.NotificationMessage;
import org.navitrace.notification.NotificatorManager;
import org.navitrace.storage.StorageException;
import org.navitrace.storage.query.Columns;
import org.navitrace.storage.query.Condition;
import org.navitrace.storage.query.Request;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationResource extends ExtendedObjectResource<Notification> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationResource.class);

    @Inject
    private NotificatorManager notificatorManager;

    public NotificationResource() {
        super(Notification.class);
    }

    @GET
    @Path("types")
    public Collection<Typed> get() {
        List<Typed> types = new LinkedList<>();
        Field[] fields = Event.class.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) && field.getName().startsWith("TYPE_")) {
                try {
                    types.add(new Typed(field.get(null).toString()));
                } catch (IllegalArgumentException | IllegalAccessException error) {
                    LOGGER.warn("Get event types error", error);
                }
            }
        }
        return types;
    }

    @GET
    @Path("notificators")
    public Collection<Typed> getNotificators(@QueryParam("announcement") boolean announcement) {
        Set<String> announcementsUnsupported = Set.of("command", "web");
        return notificatorManager.getAllNotificatorTypes().stream()
                .filter(typed -> !announcement || !announcementsUnsupported.contains(typed.type()))
                .collect(Collectors.toUnmodifiableSet());
    }

    @POST
    @Path("test")
    public Response testMessage() throws MessageException, StorageException {
        User user = permissionsService.getUser(getUserId());
        for (Typed method : notificatorManager.getAllNotificatorTypes()) {
            notificatorManager.getNotificator(method.type()).send(null, user, new Event("test", 0), null);
        }
        return Response.noContent().build();
    }

    @POST
    @Path("test/{notificator}")
    public Response testMessage(@PathParam("notificator") String notificator)
            throws MessageException, StorageException {
        User user = permissionsService.getUser(getUserId());
        notificatorManager.getNotificator(notificator).send(null, user, new Event("test", 0), null);
        return Response.noContent().build();
    }

    @POST
    @Path("send/{notificator}")
    public Response sendMessage(
            @PathParam("notificator") String notificator, @QueryParam("userId") List<Long> userIds,
            NotificationMessage message) throws MessageException, StorageException {
        permissionsService.checkAdmin(getUserId());
        List<User> users;
        if (userIds.isEmpty()) {
            users = storage.getObjects(User.class, new Request(new Columns.All()));
        } else {
            users = new ArrayList<>();
            for (long userId : userIds) {
                users.add(storage.getObject(
                        User.class, new Request(new Columns.All(), new Condition.Equals("id", userId))));
            }
        }
        for (User user : users) {
            if (!user.getTemporary()) {
                notificatorManager.getNotificator(notificator).send(user, message, null, null);
            }
        }
        return Response.noContent().build();
    }

}
