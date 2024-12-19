
package org.navitrace.forward;

public interface ResultHandler {
    void onResult(boolean success, Throwable throwable);
}
