
package org.navitrace.api.resource;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.navitrace.api.ExtendedObjectResource;
import org.navitrace.model.Driver;

@Path("drivers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DriverResource extends ExtendedObjectResource<Driver> {

    public DriverResource() {
        super(Driver.class);
    }

}
