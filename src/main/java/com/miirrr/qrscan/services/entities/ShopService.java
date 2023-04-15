package com.miirrr.qrscan.services.entities;

import com.miirrr.qrscan.entities.Shop;

import java.util.List;

public interface ShopService {
    Shop save(Shop shop);

    void saveAll(List<Shop> shops);

    void update(Shop shop);

    Shop findById(Long id);

//    Store findByName(String name);

    List<Shop> findAll();

    void deleteById(Long id);

    Shop findByName(String name);

    List<Shop> findByCity(String name);

    List<Shop> findByCityId(Long id);
}
