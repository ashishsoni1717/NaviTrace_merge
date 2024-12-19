package org.navitrace.misc;

import org.navitrace.model.Device;
import org.navitrace.model.Position;
import org.navitrace.model.Sosalarm;
import org.navitrace.storage.StorageException;

import java.util.Collection;
import java.util.List;

public abstract class DahsboardAbstract {

    public abstract List<Position> getPositions() throws StorageException;

    public abstract List<Device> getDevice() throws StorageException;

    public abstract Collection<Device> getVehicleStatus() throws StorageException;

    public abstract Collection<Sosalarm> getSosAlarmStatus() throws StorageException;
}