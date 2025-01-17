
package org.navitrace.api.resource;

import org.navitrace.api.BaseResource;
import org.navitrace.model.ObjectOperation;
import org.navitrace.config.Config;
import org.navitrace.config.Keys;
import org.navitrace.database.OpenIdProvider;
import org.navitrace.geocoder.Geocoder;
import org.navitrace.helper.Log;
import org.navitrace.helper.LogAction;
import org.navitrace.helper.model.UserUtil;
import org.navitrace.mail.MailManager;
import org.navitrace.model.Server;
import org.navitrace.model.User;
import org.navitrace.session.cache.CacheManager;
import org.navitrace.sms.SmsManager;
import org.navitrace.storage.StorageException;
import org.navitrace.storage.query.Columns;
import org.navitrace.storage.query.Condition;
import org.navitrace.storage.query.Request;

import jakarta.annotation.Nullable;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.TimeZone;

@Path("server")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ServerResource extends BaseResource {

    @Inject
    private Config config;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private MailManager mailManager;

    @Inject
    @Nullable
    private SmsManager smsManager;

    @Inject
    @Nullable
    private OpenIdProvider openIdProvider;

    @Inject
    @Nullable
    private Geocoder geocoder;

    @PermitAll
    @GET
    public Server get() throws StorageException {
        Server server = storage.getObject(Server.class, new Request(new Columns.All()));
        server.setEmailEnabled(mailManager.getEmailEnabled());
        server.setTextEnabled(smsManager != null);
        server.setGeocoderEnabled(geocoder != null);
        server.setOpenIdEnabled(openIdProvider != null);
        server.setOpenIdForce(openIdProvider != null && openIdProvider.getForce());
        User user = permissionsService.getUser(getUserId());
        if (user != null) {
            if (user.getAdministrator()) {
                server.setStorageSpace(Log.getStorageSpace());
            }
        } else {
            server.setNewServer(UserUtil.isEmpty(storage));
        }
        return server;
    }

    @PUT
    public Response update(Server server) throws Exception {
        permissionsService.checkAdmin(getUserId());
        storage.updateObject(server, new Request(
                new Columns.Exclude("id"),
                new Condition.Equals("id", server.getId())));
        cacheManager.invalidateObject(true, Server.class, server.getId(), ObjectOperation.UPDATE);
        LogAction.edit(getUserId(), server);
        return Response.ok(server).build();
    }

    @Path("geocode")
    @GET
    public String geocode(@QueryParam("latitude") double latitude, @QueryParam("longitude") double longitude) {
        if (geocoder != null) {
            return geocoder.getAddress(latitude, longitude, null);
        } else {
            throw new RuntimeException("Reverse geocoding is not enabled");
        }
    }

    @Path("timezones")
    @GET
    public Collection<String> timezones() {
        return Arrays.asList(TimeZone.getAvailableIDs());
    }

    @Path("file/{path}")
    @POST
    @Consumes("*/*")
    public Response uploadFile(@PathParam("path") String path, File inputFile) throws IOException, StorageException {
        permissionsService.checkAdmin(getUserId());
        String root = config.getString(Keys.WEB_OVERRIDE, config.getString(Keys.WEB_PATH));

        var rootPath = Paths.get(root).normalize();
        var outputPath = rootPath.resolve(path).normalize();
        if (!outputPath.startsWith(rootPath)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        var directoryPath = outputPath.getParent();
        if (directoryPath != null) {
            Files.createDirectories(directoryPath);
        }

        try (var input = new FileInputStream(inputFile); var output = new FileOutputStream(outputPath.toFile())) {
            input.transferTo(output);
        }
        return Response.ok().build();
    }

    @Path("cache")
    @GET
    public String cache() throws StorageException {
        permissionsService.checkAdmin(getUserId());
        return cacheManager.toString();
    }

    @Path("reboot")
    @POST
    public void reboot() throws StorageException {
        permissionsService.checkAdmin(getUserId());
        System.exit(130);
    }

}
