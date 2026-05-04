package com.miirrr.qrscan.repositories;

import com.miirrr.qrscan.db.DBConnector;
import com.miirrr.qrscan.entities.Position;
import com.miirrr.qrscan.entities.ProductType;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class PositionRepositoryImpl extends AbstractBaseRepository<Position> implements PositionRepository {


    @Override
    public List<Position> getByDate(LocalDateTime dateFrom, LocalDateTime dateTo, ProductType productType) {
        try (Session session = DBConnector.getSessionFactory().openSession()) {
            Query<Position> findQuery = session.createQuery(
                "SELECT p FROM Position p " +
                    "WHERE p.date BETWEEN :dateFrom AND :dateTo " +
                    "AND (p.productType = :productType OR (:includeLegacy = true AND p.productType IS NULL))",
                Position.class
            );
            findQuery.setParameter("dateFrom", dateFrom);
            findQuery.setParameter("dateTo", dateTo);
            findQuery.setParameter("productType", productType);
            findQuery.setParameter("includeLegacy", productType == ProductType.FISH);
            return findQuery.getResultList();
        }
    }

    @Override
    public List<Position> getByDateAndShopINN(LocalDateTime dateFrom, LocalDateTime dateTo, String inn, ProductType productType) {
        try (Session session = DBConnector.getSessionFactory().openSession()) {
            Query<Position> findQuery = session.createQuery(
                "SELECT p FROM Position p " +
                    "WHERE p.shop.inn = :INN " +
                    "AND p.date BETWEEN :dateFrom AND :dateTo " +
                    "AND (p.productType = :productType OR (:includeLegacy = true AND p.productType IS NULL))",
                Position.class
            );
            findQuery.setParameter("INN", inn);
            findQuery.setParameter("dateFrom", dateFrom);
            findQuery.setParameter("dateTo", dateTo);
            findQuery.setParameter("productType", productType);
            findQuery.setParameter("includeLegacy", productType == ProductType.FISH);
            return findQuery.getResultList();
        }
    }

    @Override
    public List<Position> getByDateAndShopId(LocalDateTime dateFrom, LocalDateTime dateTo, long shopId, ProductType productType) {
        try (Session session = DBConnector.getSessionFactory().openSession()) {
            Query<Position> findQuery = session.createQuery(
                "SELECT p FROM Position p " +
                    "WHERE p.shop.id = :shopId " +
                    "AND p.date BETWEEN :dateFrom AND :dateTo " +
                    "AND (p.productType = :productType OR (:includeLegacy = true AND p.productType IS NULL))",
                Position.class
            );
            findQuery.setParameter("shopId", shopId);
            findQuery.setParameter("dateFrom", dateFrom);
            findQuery.setParameter("dateTo", dateTo);
            findQuery.setParameter("productType", productType);
            findQuery.setParameter("includeLegacy", productType == ProductType.FISH);
            return findQuery.getResultList();
        }
    }

    @Override
    public Position findByName(String name, ProductType productType) {
        return findByName(name);
    }

    @Override
    public boolean existsByName(String name, ProductType productType) {
        return findByName(name) != null;
    }

    @Override
    public boolean existsByNameAndShopId(String name, Long shopId, ProductType productType) {
        try (Session session = DBConnector.getSessionFactory().openSession()) {
            Query<Boolean> query = session.createQuery(
                "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM Position p " +
                    "WHERE p.name = :NAME " +
                    "AND p.shop.id = :SHOP_ID",
                Boolean.class
            );
            query.setParameter("NAME", name);
            query.setParameter("SHOP_ID", shopId);
            return query.uniqueResult();
        }
    }

}
