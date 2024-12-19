
package org.navitrace.api.security;

public class CodeRequiredException extends SecurityException {
    public CodeRequiredException() {
        super("Code not provided");
    }
}
