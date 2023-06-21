package com.oltruong.bookstore.service;

import com.oltruong.bookstore.model.Book;
import com.oltruong.bookstore.model.Order;
import com.oltruong.bookstore.model.OrderLine;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Stateless
public class OrderService implements Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private BookService bookService;

    public List<Order> findAll() {
        TypedQuery<Order> typedQuery = entityManager.createNamedQuery(Order.FIND_ALL, Order.class);
        return typedQuery.getResultList();
    }

    public Order find(Long id) {
        return entityManager.find(Order.class, id);
    }

    public void save(Order order) {
        order.setCreationDate(new Date());
        for (OrderLine orderLine : order.getOrderLines()) {
            Book book = bookService.find(orderLine.getBook().getId());
            orderLine.setBook(book);
            entityManager.persist(orderLine);
        }
        entityManager.persist(order);
    }


}
