package com.miirrr.qrscan.repositories;

import com.miirrr.qrscan.db.DBConnector;
import com.miirrr.qrscan.entities.Shop;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ShopRepositoryImpl extends AbstractBaseRepository<Shop> implements ShopRepository {

    @Override
    public List<Shop> getByCity(String name) {
        try (Session session = DBConnector.getSessionFactory().openSession()) {
            Query<Shop> findQuery = session.createQuery("FROM "
                    + Shop.class.getSimpleName()
                    + " WHERE city.name = :NAME", Shop.class);
            findQuery.setParameter("NAME", name);
            return findQuery.getResultList();
        }
    }

    @Override
    public List<Shop> getByCityId(Long id) {
        try (Session session = DBConnector.getSessionFactory().openSession()) {
            Query<Shop> findQuery = session.createQuery("FROM "
                    + Shop.class.getSimpleName()
                    + " WHERE city.id = :ID", Shop.class);
            findQuery.setParameter("ID", id);
            return findQuery.getResultList();
        }
    }
}
