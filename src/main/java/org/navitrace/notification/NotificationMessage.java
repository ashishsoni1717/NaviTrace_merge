
package org.navitrace.notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificationMessage {

    private final String subject;
    private final String body;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public NotificationMessage(@JsonProperty("subject") String subject, @JsonProperty("body") String body) {
        this.subject = subject;
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
}
