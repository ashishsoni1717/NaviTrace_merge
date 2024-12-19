
package org.navitrace.forward;

public interface PositionForwarder {
    void forward(PositionData positionData, ResultHandler resultHandler);
}
