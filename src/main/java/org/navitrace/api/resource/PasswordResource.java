
package org.navitrace.api.resource;

import org.navitrace.api.BaseResource;
import org.navitrace.api.signature.TokenManager;
import org.navitrace.mail.MailManager;
import org.navitrace.model.User;
import org.navitrace.notification.TextTemplateFormatter;
import org.navitrace.storage.StorageException;
import org.navitrace.storage.query.Columns;
import org.navitrace.storage.query.Condition;
import org.navitrace.storage.query.Request;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.mail.MessagingException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Path("password")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class PasswordResource extends BaseResource {

    @Inject
    private MailManager mailManager;

    @Inject
    private TokenManager tokenManager;

    @Inject
    private TextTemplateFormatter textTemplateFormatter;

    @Path("reset")
    @PermitAll
    @POST
    public Response reset(@FormParam("email") String email)
            throws StorageException, MessagingException, GeneralSecurityException, IOException {

        User user = storage.getObject(User.class, new Request(
                new Columns.All(), new Condition.Equals("email", email)));
        if (user != null) {
            var velocityContext = textTemplateFormatter.prepareContext(permissionsService.getServer(), user);
            var fullMessage = textTemplateFormatter.formatMessage(velocityContext, "passwordReset", "full");
            mailManager.sendMessage(user, true, fullMessage.getSubject(), fullMessage.getBody());
        }
        return Response.ok().build();
    }

    @Path("update")
    @PermitAll
    @POST
    public Response update(
            @FormParam("token") String token, @FormParam("password") String password)
            throws StorageException, GeneralSecurityException, IOException {

        long userId = tokenManager.verifyToken(token).getUserId();
        User user = storage.getObject(User.class, new Request(
                new Columns.All(), new Condition.Equals("id", userId)));
        if (user != null) {
            user.setPassword(password);
            storage.updateObject(user, new Request(
                    new Columns.Include("hashedPassword", "salt"),
                    new Condition.Equals("id", userId)));
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
