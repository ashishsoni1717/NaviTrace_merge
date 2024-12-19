package org.navitrace.api.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.navitrace.BaseProtocolEncoder;
import org.navitrace.api.BaseResource;
import org.navitrace.model.BaseModel;
import org.navitrace.model.VehiclesList;
import org.navitrace.storage.StorageException;
import org.navitrace.storage.query.Columns;
import org.navitrace.storage.query.Order;
import org.navitrace.storage.query.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

@Path("vehicle_list")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleListResource <T extends BaseModel> extends BaseResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseProtocolEncoder.class);

//    public DashboardResource(Class<Device> baseClass) {
//        super(baseClass);
//    }


    @Path("/getVehicleList")
    @GET
    public Response get(@QueryParam("pageNo") int pageNo,
                        @QueryParam("pageSize") int pageSize,
                        @QueryParam("sortField") String sortField,
                        @QueryParam("desc") boolean desc) throws StorageException {
        Collection<VehiclesList> vlists = storage.getVehicleListStorage(VehiclesList.class,
                new Request(new Columns.All(), new Order(sortField, desc, pageNo, pageSize)));
        return Response.ok(vlists).build();
    }
}