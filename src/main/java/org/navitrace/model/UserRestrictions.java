
package org.navitrace.model;

public interface UserRestrictions {
    boolean getReadonly();
    boolean getDeviceReadonly();
    boolean getLimitCommands();
    boolean getDisableReports();
    boolean getFixedEmail();
}
