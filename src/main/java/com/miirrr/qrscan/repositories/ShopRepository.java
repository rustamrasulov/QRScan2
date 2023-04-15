package com.miirrr.qrscan.repositories;

import com.miirrr.qrscan.entities.Shop;

import java.util.List;

public interface ShopRepository extends BaseRepository<Shop>{

    List<Shop> getByCity(String name);

    List<Shop> getByCityId(Long id);
}
