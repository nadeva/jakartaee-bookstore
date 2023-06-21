package com.oltruong.bookstore.util;


import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;


public class DatabaseProducer {

    @Produces
    @PersistenceContext(unitName = "jakarta-application-persistence-unit", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;
}
