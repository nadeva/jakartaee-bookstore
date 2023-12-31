package com.oltruong.bookstore.rest;

import com.oltruong.bookstore.model.Order;
import com.oltruong.bookstore.service.OrderService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

/**
 * @author Olivier Truong
 */
@Path("/orders")
public class OrderEndPoint {

    @Inject
    private OrderService orderService;

    @POST
    @Consumes("application/json")
    public Response create(Order order) {
        orderService.save(order);
        return Response.created(UriBuilder.fromResource(OrderEndPoint.class).path(String.valueOf(order.getId())).build()).build();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces("application/json")
    public Response findById(@PathParam("id") Long id) {
        Order entity = orderService.find(id);

        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(entity).build();
    }

    @GET
    @Produces("application/json")
    public Response listAll() {
        return Response.ok(orderService.findAll()).build();
    }
}
