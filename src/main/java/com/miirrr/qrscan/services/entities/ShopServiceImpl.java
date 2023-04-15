package com.miirrr.qrscan.services.entities;

import com.miirrr.qrscan.entities.Shop;
import com.miirrr.qrscan.repositories.ShopRepository;
import com.miirrr.qrscan.repositories.ShopRepositoryImpl;

import java.util.List;

public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;

    public ShopServiceImpl() {
        shopRepository = new ShopRepositoryImpl();
    }

    @Override
    public Shop save(Shop shop) {
        return shopRepository.save(shop);
    }

    @Override
    public void saveAll(List<Shop> shops) {
        shopRepository.saveAll(shops);
    }

    @Override
    public void update(Shop shop) {
        shopRepository.save(shop);
    }

    @Override
    public Shop findById(Long id) {
        return shopRepository.findById(id);
    }

    @Override
    public List<Shop> findAll() {
        return shopRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        shopRepository.deleteById(id);
    }

    @Override
    public Shop findByName(String name) {
        return shopRepository.findByName(name);
    }

    @Override
    public List<Shop> findByCity(String name) {
        return shopRepository.getByCity(name);
    }

    @Override
    public List<Shop> findByCityId(Long id) {
        return shopRepository.getByCityId(id);
    }
}
