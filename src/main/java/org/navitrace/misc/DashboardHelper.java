package org.navitrace.misc;

import jakarta.inject.Inject;
import org.navitrace.*;
import org.navitrace.api.BaseResource;
import org.navitrace.model.*;
//import org.navitrace.model.VehicleStatus;
import org.navitrace.storage.DatabaseStorage;
import org.navitrace.storage.Storage;
import org.navitrace.storage.StorageException;
import org.navitrace.storage.query.Columns;
import org.navitrace.storage.query.Condition;
import org.navitrace.storage.query.Request;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardHelper extends BaseResource {

    private static final String NUM_SOS_PENDING = "num_sos_pending" ;
    private static final String NUM_SOS_RESOLVED = "num_sos_resolved" ;
    private static final String NUM_SOS_CANCELED = "num_sos_canceled" ;
    private static final String NUM_SOS_ACKNOWLEDGED = "num_sos_acknowledged" ;
    private static Device device;
    @Inject
    protected DahsboardAbstract dahsboardAbstract;

    public DeviceManager getDeviceManager() throws StorageException {
        DeviceManager manager= new DeviceManager();

        List<Device> deviceList = dahsboardAbstract.getDevice();
        List<Position> positionList = dahsboardAbstract.getPositions();
        manager.setDevice(deviceList);
        manager.setPosition(positionList);
        return manager;
    }

    public VehicleStatus getVehicleStatus() throws StorageException{
        VehicleStatus vehicleStatusManager = new VehicleStatus();

        List<Device> vehicleStatusList = (List<Device>) dahsboardAbstract.getVehicleStatus();
        vehicleStatusManager.setVehicleStatus(vehicleStatusList);
        return (VehicleStatus) vehicleStatusList;
    }

//    public SosalarmStatus getSosalarmStatus() {
//        SosalarmStatus sosalarmStatusManager = new Sosalarm();
//        List<Sosalarm> sosalarmsList = (List<Sosalarm>) dahsboardAbstract.getSosalarmStatus();
//
//        return sosalarmStatusManager;
//    }
}
