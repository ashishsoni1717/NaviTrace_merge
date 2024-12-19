
package org.navitrace.api.resource;

import org.navitrace.api.SimpleObjectResource;
import org.navitrace.model.Order;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource extends SimpleObjectResource<Order> {

    public OrderResource() {
        super(Order.class);
    }

}
