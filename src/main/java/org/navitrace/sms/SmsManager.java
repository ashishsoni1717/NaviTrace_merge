
package org.navitrace.sms;

import org.navitrace.notification.MessageException;

public interface SmsManager {

    void sendMessage(String phone, String message, boolean command) throws MessageException;

}
