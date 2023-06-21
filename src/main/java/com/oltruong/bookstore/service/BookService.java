package com.oltruong.bookstore.service;


import com.oltruong.bookstore.generator.ISBNGenerator;
import com.oltruong.bookstore.model.Book;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class BookService implements Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private ISBNGenerator isbnGenerator;


    public Book find(Long id) {
        return entityManager.find(Book.class, id);
    }

    public void save(Book book) {
        book.setIsbn(isbnGenerator.generateISBN());
        entityManager.persist(book);
    }

    public List<Book> findAll() {
        TypedQuery<Book> typedQuery = entityManager.createNamedQuery(Book.FIND_ALL, Book.class);
        return typedQuery.getResultList();
    }

    public void delete(Book book) {
        Book bookInDatabase = entityManager.find(Book.class, book.getId());
        if (bookInDatabase != null) {
            entityManager.remove(bookInDatabase);
        }
    }

    public Book update(Book book) {
        return entityManager.merge(book);
    }

    public void flush() {
        entityManager.flush();
    }

    public List<Book> searchBooks(Book book) {


        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

        CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
        Root<Book> root = criteria.from(Book.class);
        TypedQuery<Book> query = this.entityManager.createQuery(criteria
                .select(root).where(getSearchPredicates(root, book)));
        return query.getResultList();


    }

    private Predicate[] getSearchPredicates(Root<Book> root, Book example) {

        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<Predicate>();

        String isbn = example.getIsbn();
        if (isbn != null && !"".equals(isbn)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("isbn")), '%' + isbn.toLowerCase() + '%'));
        }
        String name = example.getName();
        if (name != null && !"".equals(name)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("name")), '%' + name.toLowerCase() + '%'));
        }
        String description = example.getDescription();
        if (description != null && !"".equals(description)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("description")), '%' + description.toLowerCase() + '%'));
        }
        String pictureURL = example.getPictureURL();
        if (pictureURL != null && !"".equals(pictureURL)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("pictureURL")), '%' + pictureURL.toLowerCase() + '%'));
        }
        String author = example.getAuthor();
        if (author != null && !"".equals(author)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("author")), '%' + author.toLowerCase() + '%'));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }

}
