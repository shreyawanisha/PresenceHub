package org.attendance.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public abstract class GenericDAOImpl<T> implements GenericDAO<T> {
    @PersistenceContext
    protected EntityManager em;

    private final Class<T> entityClass;

    public GenericDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    @Transactional
    public void save(T entity) {
        em.persist(entity);
    }

    @Override
    public T findById(Long id) {
        return em.find(entityClass, id);
    }

    @Override
    @Transactional
    public void update(T entity) {
        em.merge(entity);
    }

    @Override
    @Transactional
    public void delete(T entity) {
        em.remove(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        T entity = findById(id);
        if (entity != null) {
            em.remove(entity);
        }
    }

    @Override
    public List<T> findAll() {
        return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass).getResultList();
    }
}
