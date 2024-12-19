package org.navitrace.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviceManager {
    private List<Device> device;
    private List<Position> position;

//    public List<VehicleStatus> getVehicleStatus() {
//        return vehicleStatus;
//    }
//
//    public void setVehicleStatus(List<VehicleStatus> vehicleStatus) {
//        this.vehicleStatus = vehicleStatus;
//    }
//
//    private List <VehicleStatus> vehicleStatus;

    public List<Device> getDevice() {
        return device;
    }

    public void setDevice(List<Device> device) {
        this.device = device;
    }

    public List<Position> getPosition() {
        return position;
    }

    public void setPosition(List<Position> position) {
        this.position = position;
    }
    public Set<Long> getAllItems() {

        return null;
    }
}
