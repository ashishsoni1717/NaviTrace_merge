
package org.navitrace.geocoder;

import org.navitrace.database.StatisticsManager;

public interface Geocoder {

    interface ReverseGeocoderCallback {

        void onSuccess(String address);

        void onFailure(Throwable e);

    }

    String getAddress(double latitude, double longitude, ReverseGeocoderCallback callback);

    void setStatisticsManager(StatisticsManager statisticsManager);

}
