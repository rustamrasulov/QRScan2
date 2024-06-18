package com.miirrr.qrscan.repositories;

import com.miirrr.qrscan.db.DBConnector;
import com.miirrr.qrscan.entities.Position;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class PositionRepositoryImpl extends AbstractBaseRepository<Position> implements PositionRepository {


    @Override
    public List<Position> getByDate(LocalDateTime dateFrom, LocalDateTime dateTo) {
        try (Session session = DBConnector.getSessionFactory().openSession()) {
            Query<Position> findQuery = session.createQuery(
                "SELECT p FROM Position p WHERE date BETWEEN :dateFrom AND :dateTo",
                Position.class
            );
            findQuery.setParameter("dateFrom", dateFrom);
            findQuery.setParameter("dateTo", dateTo);
            return findQuery.getResultList();
        }
    }

    @Override
    public List<Position> getByDateAndShopINN(LocalDateTime dateFrom, LocalDateTime dateTo, String inn) {
        try (Session session = DBConnector.getSessionFactory().openSession()) {
            Query<Position> findQuery = session.createQuery(
                "SELECT p FROM Position p WHERE shop.inn = :INN AND p.date BETWEEN :dateFrom AND :dateTo",
                Position.class
            );
            findQuery.setParameter("INN", inn);
            findQuery.setParameter("dateFrom", dateFrom);
            findQuery.setParameter("dateTo", dateTo);
            return findQuery.getResultList();
        }
    }

    @Override
    public List<Position> getByDateAndShopId(LocalDateTime dateFrom, LocalDateTime dateTo, long shopId) {
        try (Session session = DBConnector.getSessionFactory().openSession()) {
            Query<Position> findQuery = session.createQuery(
                "SELECT p FROM Position p WHERE shop.id = :shopId AND p.date BETWEEN :dateFrom AND :dateTo",
                Position.class
            );
            findQuery.setParameter("shopId", shopId);
            findQuery.setParameter("dateFrom", dateFrom);
            findQuery.setParameter("dateTo", dateTo);
            return findQuery.getResultList();
        }
    }

    @Override
    public boolean existsByName(String name) {
        try (Session session = DBConnector.getSessionFactory().openSession()) {
            Query<Boolean> query = session.createQuery(
                "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM Position p WHERE p.name = :NAME",
                Boolean.class
            );
            query.setParameter("NAME", name);
            return query.uniqueResult();
        }
    }

    @Override
    public boolean existsByNameAndShopId(String name, Long shopId) {
        try (Session session = DBConnector.getSessionFactory().openSession()) {
            Query<Boolean> query = session.createQuery(
                "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM Position p WHERE p.name = :NAME AND p.shop.id = :SHOP_ID",
                Boolean.class
            );
            query.setParameter("NAME", name);
            query.setParameter("SHOP_ID", shopId);
            return query.uniqueResult();
        }
    }

}
