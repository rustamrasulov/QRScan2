package com.miirrr.qrscan.repositories;

import com.miirrr.qrscan.db.DBConnector;
import com.miirrr.qrscan.entities.Position;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public class PositionRepositoryImpl extends AbstractBaseRepository<Position> implements PositionRepository {


    @Override
    public List<Position> getByDate(LocalDateTime dateFrom, LocalDateTime dateTo) {
        try (Session session = DBConnector.getSessionFactory().openSession()) {
            Query<Position> findQuery = session.createQuery("SELECT p FROM Position p WHERE date BETWEEN :dateFrom AND :dateTo", Position.class);
            findQuery.setParameter("dateFrom", dateFrom);
            findQuery.setParameter("dateTo", dateTo);
            return findQuery.getResultList();
        }
    }

    @Override
    public List<Position> getByDateAndShopINN(LocalDateTime dateFrom, LocalDateTime dateTo, String inn) {
        try (Session session = DBConnector.getSessionFactory().openSession()) {
            Query<Position> findQuery = session.createQuery("SELECT p FROM Position p WHERE shop.inn = :INN AND p.date BETWEEN :dateFrom AND :dateTo", Position.class);
            findQuery.setParameter("INN", inn);
            findQuery.setParameter("dateFrom", dateFrom);
            findQuery.setParameter("dateTo", dateTo);
            return findQuery.getResultList();
        }
    }

    @Override
    public List<Position> getByDateAndShopId(LocalDateTime dateFrom, LocalDateTime dateTo, long shopId) {
        try (Session session = DBConnector.getSessionFactory().openSession()) {
            Query<Position> findQuery = session.createQuery("SELECT p FROM Position p WHERE shop.id = :shopId AND p.date BETWEEN :dateFrom AND :dateTo", Position.class);
            findQuery.setParameter("shopId", shopId);
            findQuery.setParameter("dateFrom", dateFrom);
            findQuery.setParameter("dateTo", dateTo);
            return findQuery.getResultList();
        }
    }
}
