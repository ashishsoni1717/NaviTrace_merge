
package org.navitrace.forward;

public interface EventForwarder {
    void forward(EventData eventData, ResultHandler resultHandler);
}
