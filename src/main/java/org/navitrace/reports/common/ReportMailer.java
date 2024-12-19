
package org.navitrace.reports.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.navitrace.api.security.PermissionsService;
import org.navitrace.mail.MailManager;
import org.navitrace.model.User;
import org.navitrace.storage.StorageException;

import jakarta.activation.DataHandler;
import jakarta.inject.Inject;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ReportMailer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportMailer.class);

    private final PermissionsService permissionsService;
    private final MailManager mailManager;

    @Inject
    public ReportMailer(PermissionsService permissionsService, MailManager mailManager) {
        this.permissionsService = permissionsService;
        this.mailManager = mailManager;
    }

    public void sendAsync(long userId, ReportExecutor executor) {
        new Thread(() -> {
            try {
                var stream = new ByteArrayOutputStream();
                executor.execute(stream);

                MimeBodyPart attachment = new MimeBodyPart();
                attachment.setFileName("report.xlsx");
                attachment.setDataHandler(new DataHandler(new ByteArrayDataSource(
                        stream.toByteArray(), "application/octet-stream")));

                User user = permissionsService.getUser(userId);
                mailManager.sendMessage(user, false, "Report", "The report is in the attachment.", attachment);
            } catch (StorageException | IOException | MessagingException e) {
                LOGGER.warn("Email report failed", e);
            }
        }).start();
    }

}
