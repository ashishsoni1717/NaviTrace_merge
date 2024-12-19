
package org.navitrace.storage;

import org.navitrace.model.*;
import org.navitrace.storage.query.Request;

import java.util.Collection;
import java.util.List;

public abstract class Storage {

    public abstract <T> List<T> getObjects(Class<T> clazz, Request request) throws StorageException;

    // Vehicle Status///
    public abstract <T> List<T> getVehicleStatusStorage(Class<T> clazz, Request request) throws StorageException;
//    Vehicle list
    public abstract <T> List<T> getVehicleListStorage(Class<T> clazz, Request request) throws StorageException;

    // Device Data//
    public abstract <T> List<T> getDeviceStatusStorage(Class<T> clazz, Request request) throws StorageException;
    public abstract<T> List<T> getStatisticsStorage(Class<T> clazz, Request request) throws StorageException;
    public abstract<T> List<T> getPanicStatusStorage(Class<T> clazz, Request request) throws StorageException;
    public  abstract<T> List<T> getVehicleSummaryStorage(Class<T> clazz, Request request) throws StorageException;
    public abstract<T> List<T> getTripDataStorage(Class<T> clazz, Request request) throws StorageException;
    public abstract<T> List<T> getTodayEventsStorage(Class<T> clazz, Request request) throws StorageException;
    public abstract<T> List<T> getTotalMonthlyDistanceStorage(Class<T> clazz, Request request) throws StorageException;
    public abstract <T> long addObject(T entity, Request request) throws StorageException;

    public abstract <T> void updateObject(T entity, Request request) throws StorageException;

    public abstract void removeObject(Class<?> clazz, Request request) throws StorageException;

    public abstract List<Permission> getPermissions(
            Class<? extends BaseModel> ownerClass, long ownerId,
            Class<? extends BaseModel> propertyClass, long propertyId) throws StorageException;

    public abstract void addPermission(Permission permission) throws StorageException;

    public abstract void removePermission(Permission permission) throws StorageException;

    public List<Permission> getPermissions(
            Class<? extends BaseModel> ownerClass,
            Class<? extends BaseModel> propertyClass) throws StorageException {
        return getPermissions(ownerClass, 0, propertyClass, 0);
    }

    public <T> T getObject(Class<T> clazz, Request request) throws StorageException {
        var objects = getObjects(clazz, request);
        return objects.isEmpty() ? null : objects.get(0);
    }

}
