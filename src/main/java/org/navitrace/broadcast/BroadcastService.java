
package org.navitrace.broadcast;

import org.navitrace.LifecycleObject;

public interface BroadcastService extends LifecycleObject, BroadcastInterface {
    boolean singleInstance();
    void registerListener(BroadcastInterface listener);
}
