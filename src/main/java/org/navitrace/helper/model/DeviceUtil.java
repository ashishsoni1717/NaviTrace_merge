
package org.navitrace.helper.model;

import org.navitrace.model.Device;
import org.navitrace.model.Group;
import org.navitrace.model.User;
import org.navitrace.storage.Storage;
import org.navitrace.storage.StorageException;
import org.navitrace.storage.query.Columns;
import org.navitrace.storage.query.Condition;
import org.navitrace.storage.query.Request;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

public final class DeviceUtil {

    private DeviceUtil() {
    }

    public static void resetStatus(Storage storage) throws StorageException {
        storage.updateObject(new Device(), new Request(new Columns.Include("status")));
    }


    public static Collection<Device> getAccessibleDevices(
            Storage storage, long userId,
            Collection<Long> deviceIds, Collection<Long> groupIds) throws StorageException {

        var devices = storage.getObjects(Device.class, new Request(
                new Columns.All(),
                new Condition.Permission(User.class, userId, Device.class)));
        var deviceById = devices.stream()
                .collect(Collectors.toUnmodifiableMap(Device::getId, x -> x));
        var devicesByGroup = devices.stream()
                .filter(x -> x.getGroupId() > 0)
                .collect(Collectors.groupingBy(Device::getGroupId));

        var groups = storage.getObjects(Group.class, new Request(
                new Columns.All(),
                new Condition.Permission(User.class, userId, Group.class)));
        var groupsByGroup = groups.stream()
                .filter(x -> x.getGroupId() > 0)
                .collect(Collectors.groupingBy(Group::getGroupId));

        var results = deviceIds.stream()
                .map(deviceById::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        var groupQueue = new LinkedList<>(groupIds);
        while (!groupQueue.isEmpty()) {
            long groupId = groupQueue.pop();
            results.addAll(devicesByGroup.getOrDefault(groupId, Collections.emptyList()));
            groupQueue.addAll(groupsByGroup.getOrDefault(groupId, Collections.emptyList())
                    .stream().map(Group::getId).toList());
        }

        return results;
    }

}
