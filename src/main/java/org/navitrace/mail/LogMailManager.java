
package org.navitrace.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.navitrace.model.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;

public class LogMailManager implements MailManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogMailManager.class);

    @Override
    public boolean getEmailEnabled() {
        return true;
    }

    @Override
    public void sendMessage(
            User user, boolean system, String subject, String body) throws MessagingException {
        sendMessage(user, system, subject, body, null);
    }

    @Override
    public void sendMessage(
            User user, boolean system, String subject, String body, MimeBodyPart attachment) throws MessagingException {
        LOGGER.info(
                "Email sent\nTo: {}\nSubject: {}\nAttachment: {}\nBody:\n{}",
                user.getEmail(), subject, attachment != null ? attachment.getFileName() : null, body);
    }

}
