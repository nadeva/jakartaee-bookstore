package com.oltruong.bookstore.rest;

import com.oltruong.bookstore.model.Book;
import com.oltruong.bookstore.service.BookService;
import jakarta.inject.Inject;
import jakarta.persistence.OptimisticLockException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriBuilder;

/**
 * @author Olivier Truong
 */
@Path("/books")
public class BookEndpoint {


    @Inject
    private BookService bookService;

    @POST
    @Consumes("application/json")
    public Response create(Book entity) {
        bookService.save(entity);
        return Response.created(UriBuilder.fromResource(BookEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteById(@PathParam("id") Long id) {
        Book entity = bookService.find(id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        bookService.delete(entity);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces("application/json")
    public Response findById(@PathParam("id") Long id) {
        Book entity = bookService.find(id);

        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(entity).build();
    }

    @GET
    @Produces("application/json")
    public Response listAll() {
        return Response.ok(bookService.findAll()).build();
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes("application/json")
    public Response update(Book entity) {
        try {
            entity = bookService.update(entity);
        } catch (OptimisticLockException e) {
            return Response.status(Status.CONFLICT).entity(e.getEntity()).build();
        }

        return Response.noContent().build();
    }
}
