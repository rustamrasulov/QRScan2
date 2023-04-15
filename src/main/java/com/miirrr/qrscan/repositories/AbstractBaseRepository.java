package com.miirrr.qrscan.repositories;

import com.miirrr.qrscan.db.DBConnector;
import com.miirrr.qrscan.entities.BaseEntity;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.MappedSuperclass;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Log4j2
@MappedSuperclass
public abstract class AbstractBaseRepository<T extends BaseEntity> implements BaseRepository<T> {

    @Override
    public T save(T entity) {
        Transaction transaction = null;
        try (Session session = DBConnector.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            if (entity.isNew()) {
                session.persist(entity);
            } else {
                session.merge(entity);
            }
            transaction.commit();
        } catch (Exception e) {
            if(transaction != null) transaction.rollback();
        }
        return entity;
    }

    @Override
    public void saveAll(List<T> entities) {
        for (T entity : entities) {
            save(entity);
        }
    }


    @Override
    public T findById(Long id) {
        try (Session session = DBConnector.getSessionFactory().openSession()) {
            return session.find(entityClass(), id);
        }
    }

    public T findByName(String name) {
        try (Session session = DBConnector.getSessionFactory().openSession()) {
            Query<T> findQuery = session.createQuery("FROM " + entityClass().getSimpleName() + " e WHERE e.name = :NAME", entityClass());
            findQuery.setParameter("NAME", name);
            return findQuery.uniqueResultOptional().orElse(null);
        }
    }

    @Override
    public List<T> findAll() {
        try (Session session = DBConnector.getSessionFactory().openSession()) {
            return session.createQuery("FROM " + entityClass().getSimpleName(), entityClass()).getResultList();
        }
    }

    @Override
    public void deleteById(Long id) {
        Transaction transaction = null;
        try (Session session = DBConnector.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.createQuery("DELETE FROM " + entityClass().getSimpleName() + " WHERE id = :ID")
                    .setParameter("ID", id)
                    .executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
        }
    }

    @SuppressWarnings("unchecked")
    private Class<T> entityClass() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }
}
