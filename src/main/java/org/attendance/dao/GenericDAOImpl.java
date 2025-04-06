package org.attendance.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.Session;


import java.util.List;
import java.util.Optional;

public abstract class GenericDAOImpl<T> implements GenericDAO<T> {

    @Autowired
    private SessionFactory sessionFactory;

    private final Class<T> entityClass;

    public GenericDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(T entity) {
        getSession().persist(entity);
    }

    @Override
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(getSession().find(entityClass, id));
    }

    @Override
    public void update(T entity) {
        getSession().merge(entity);
    }

    @Override
    public void delete(T entity) {
        getSession().remove(entity);
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(entity -> getSession().remove(entity));
    }

    @Override
    public List<T> findAll() {
        return getSession().createQuery("FROM " + entityClass.getSimpleName(), entityClass).getResultList();
    }
}