package org.attendance.dao;

import jakarta.transaction.Transactional;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.hibernate.Session;


import java.util.List;

@Repository
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
    @Transactional
    public void save(T entity) {
        getSession().persist(entity);
    }

    @Override
    public T findById(Long id) {
        return getSession().find(entityClass, id);
    }

    @Override
    @Transactional
    public void update(T entity) {
        getSession().merge(entity);
    }

    @Override
    @Transactional
    public void delete(T entity) {
        getSession().remove(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        T entity = findById(id);
        if (entity != null) {
            getSession().remove(entity);
        }
    }

    @Override
    public List<T> findAll() {
        return getSession().createQuery("FROM " + entityClass.getSimpleName(), entityClass).getResultList();
    }
}
