package org.navitrace.api.resource;
import org.navitrace.BaseProtocolEncoder;
import org.navitrace.api.BaseResource;
import org.navitrace.misc.DashboardHelper;
//import org.navitrace.misc.VehicleStatus;
import org.navitrace.model.*;
import org.navitrace.storage.StorageException;
import org.navitrace.storage.query.Columns;
import org.navitrace.storage.query.Condition;
import org.navitrace.storage.query.Order;
import org.navitrace.storage.query.Request;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;
import java.util.Calendar;

@Path("dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DashboardResource <T extends BaseModel> extends BaseResource {
    private static final String SOS_KEY_SOS = "sos";
    private static final String NUM_SOS_PENDING = "num_sos_pending";
    private static final String NUM_SOS_RESOLVED = "num_sos_resolved";
    private static final String TOTAL_SOS = "total_sos";
    private static final String TOTAL_DEVICES = "total_devices";
    private static final String UNKNOWN = "unknown";
    private static final String KEY_ALARM = "alarm";
    private static final int WEEK = 7;
    private static int count;

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseProtocolEncoder.class);

//    public DashboardResource(Class<Device> baseClass) {
//        super(baseClass);
//    }

    @Path("/deviceManager")
    @GET
    public DeviceManager getDeviceManager(
            @QueryParam("count(all)") boolean count,
            @QueryParam("deviceId") long deviceId,
            @QueryParam("year") long year,
            @QueryParam("userId") long userId,
            @QueryParam("from") Date from,
            @QueryParam("to") Date to)
            throws SQLException, StorageException {
//        if (!RoleK.isValidGetOperation(RoleK.KEY_DASHBOARD, getUserId())) {
//            throw new SecurityException("Role not assigned to the user.");
//        }
        DeviceManager deviceManager = new DashboardHelper().getDeviceManager();

        return deviceManager;
    }

    // With Query from DatabaseStorage
//    @Path("/getVehicleStatus")
//    @GET
//    public VehicleStatus getVehicleStatus() throws StorageException {
//
//        VehicleStatus vehicleStatus = new DashboardHelper().getVehicleStatus();
//        return vehicleStatus;
//    }

    // ********** get Device Status final ***********//
    @Path("/getDeviceStatus")
    @GET
    public Collection<DeviceData> get(
            @QueryParam("all") boolean all,
            @QueryParam("userId") long userId
    )throws StorageException{
        var conditions = new LinkedList<Condition>();

        if (all) {
            if (permissionsService.notAdmin(getUserId())) {
                conditions.add(new Condition.Permission(User.class, getUserId(), Device.class));
            }
        } else {
            if (userId == 0) {
                conditions.add(new Condition.Permission(User.class, getUserId(), Device.class));
            } else {
                permissionsService.checkUser(getUserId(), userId);
                conditions.add(new Condition.Permission(User.class, userId, Device.class).excludeGroups());
            }
        }

        //LOGGER.warn("getDeviceStatus");
        return storage.getDeviceStatusStorage(DeviceData.class, new Request(new Columns.All(), Condition.merge(conditions)));
    }

    // ********** getDevice Statistics ***********//
    @Path("/getDeviceStatistics")
    @GET

    public Collection<Statistics> get(
            @QueryParam("from") Date from,
            @QueryParam("to") Date to) throws StorageException {
        permissionsService.checkAdmin(getUserId());
        //LOGGER.warn("getDeviceStatus");
        return storage.getStatisticsStorage(Statistics.class, new Request(
                new Columns.All(),
                new Condition.Between("captureTime", "from", from, "to", to),
                new Order("captureTime")));
    }

    // ********** getVehicle Status final ***********//
    @Path("/getVehicleStatus")
    @GET

    public Collection<DeviceStatusCount> get(
            @QueryParam("all") boolean all,
            @QueryParam("userId") long userId,
          @QueryParam("count(all)") long count,
          @QueryParam("id") List<Long> deviceIds,
            @QueryParam("status") List<String> status
    ) throws StorageException{
        var conditions = new LinkedList<Condition>();
        if (all) {
            if (permissionsService.notAdmin(getUserId())) {
                conditions.add(new Condition.Permission(User.class, getUserId(), Device.class));
            }
        } else {
            if (userId == 0) {
                conditions.add(new Condition.Permission(User.class, getUserId(), Device.class));
            } else {
                permissionsService.checkUser(getUserId(), userId);
                conditions.add(new Condition.Permission(User.class, userId, Device.class).excludeGroups());
            }
        }

        LOGGER.warn("working DeviceStatusCount");

        return storage.getVehicleStatusStorage(DeviceStatusCount.class, new Request(new Columns.All(), Condition.merge(conditions)));
    }

    // ********** getPanicStatus for dashboard ***********//
    @Path("/getPanicStatus")
    @GET
    public Collection<Sosalarm> get(
            @QueryParam("all") boolean all,
            @QueryParam("uniqueId") List<String> uniqueIds,
            @QueryParam("userId") long userId,
            @QueryParam("count") long count,
            @QueryParam("id") List<Long> deviceIds) throws StorageException{

        var conditions = new LinkedList<Condition>();

        if(all){
            if (permissionsService.notAdmin(getUserId())) {
                conditions.add(new Condition.Permission(User.class, getUserId(), Device.class));
            }
        } else {
            if (userId == 0) {
                conditions.add(new Condition.Permission(User.class, getUserId(), Device.class));
            } else {
                permissionsService.checkUser(getUserId(), userId);
                conditions.add(new Condition.Permission(User.class, userId, Device.class).excludeGroups());
            }
        }
        return storage.getPanicStatusStorage(Sosalarm.class, new Request(new Columns.All(), Condition.merge(conditions)));

    }

    // ********** get Todays Events for dashboard ***********//

    @Path("/getTodaysEvents")
    @GET
    public Collection<Event> get(
            @QueryParam("all") boolean all,
            @QueryParam("deviceId")List<Long> deviceIds,
            @QueryParam("userId") long userId
    ) throws StorageException {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, -1);
        Date lastDay = calendar.getTime();
        //Collection<Event> events ;
        var conditions = new LinkedList<Condition>();
        if (all) {
            if (permissionsService.notAdmin(getUserId())) {
                conditions.add(new Condition.Permission(User.class, getUserId(), Device.class));
            }
        } else {
            if (userId == 0) {
                conditions.add(new Condition.Permission(User.class, getUserId(), Device.class));
            } else {
                permissionsService.checkUser(getUserId(), userId);
                conditions.add(new Condition.Permission(User.class, userId, Device.class).excludeGroups());
            }
        }
        LOGGER.warn("working getTodaysEvents");
        return storage.getTodayEventsStorage(Event.class, new Request(new Columns.All(), Condition.merge(conditions)));

    }

    // ********** get Trips Data for dashboard ***********//
    @Path("/getTripData")
    @GET

    public Collection<Trip> get(
            @QueryParam("all") boolean all, @QueryParam("userId") long userId,
            @QueryParam("searchKey") String searchKey,
            @QueryParam("currentPage") int currentPage,
            @QueryParam("pageSize") int pageSize)  throws StorageException{
        var conditions = new LinkedList<Condition>();

        if(all){
            if (permissionsService.notAdmin(getUserId())) {
                conditions.add(new Condition.Permission(User.class, getUserId(), Device.class));
            }
        } else {
            if (userId == 0) {
                conditions.add(new Condition.Permission(User.class, getUserId(), Device.class));
            } else {
                permissionsService.checkUser(getUserId(), userId);
                conditions.add(new Condition.Permission(User.class, userId, Device.class).excludeGroups());
            }
        }
        return storage.getTripDataStorage(Trip.class, new Request(new Columns.All(), Condition.merge(conditions)));

    }

    // ********** getVehicle Summary for dashboard ***********//
    @Path("/getVehicleSummary")
    @GET
    public Collection<VehicleSummary> get(
            @QueryParam("deviceId")List<Long> deviceIds, @QueryParam("groupId") List<Long> groupIds,
            @QueryParam("searchKey") String searchKey, @QueryParam("from") Date from,
            @QueryParam("to") Date to, @QueryParam("currentPage") int currentPage,
            @QueryParam("userId") long userId,  @QueryParam("all") boolean all,
            @QueryParam("pageSize") int pageSize, @QueryParam("sortField") String sortField,
            @QueryParam("asc") Boolean asc) throws StorageException {
        var conditions = new LinkedList<Condition>();
        if (all) {
            if (permissionsService.notAdmin(getUserId())) {
                conditions.add(new Condition.Permission(User.class, getUserId(), Device.class));
            }
        } else {
            if (userId == 0) {
                conditions.add(new Condition.Permission(User.class, getUserId(), Device.class));
            } else {
                permissionsService.checkUser(getUserId(), userId);
                conditions.add(new Condition.Permission(User.class, userId, Device.class).excludeGroups());
            }
        }
        LOGGER.warn("working getVehicleSummary");
        return storage.getVehicleSummaryStorage(VehicleSummary.class, new Request(new Columns.All(), Condition.merge(conditions)));
    }

    @Path("/getTotalDistance")
    @GET
    public Collection<TotalMonthlyDistance> get(
            @QueryParam("userId") long userId,  @QueryParam("all") boolean all,
            @QueryParam("totalDistance") double totalDistance,
            @QueryParam("tcmonth") long tcmonth
    ) throws StorageException{
        var conditions = new LinkedList<Condition>();
        if (all) {
            if (permissionsService.notAdmin(getUserId())) {
                conditions.add(new Condition.Permission(User.class, getUserId(), Device.class));
            }
        } else {
            if (userId == 0) {
                conditions.add(new Condition.Permission(User.class, getUserId(), Device.class));
            } else {
                permissionsService.checkUser(getUserId(), userId);
                conditions.add(new Condition.Permission(User.class, userId, Device.class).excludeGroups());
            }
        }
        LOGGER.warn("working getTotalDistance");
        return storage.getTotalMonthlyDistanceStorage(TotalMonthlyDistance.class, new Request(new Columns.All(), Condition.merge(conditions)));
    }

}