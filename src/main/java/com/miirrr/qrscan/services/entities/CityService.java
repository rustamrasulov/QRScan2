package com.miirrr.qrscan.services.entities;

import com.miirrr.qrscan.entities.City;

import java.util.List;

public interface CityService {
    City save(City city);

    void saveAll(List<City> cities);

    void update(City city);

    City findById(Long id);

    City findByName(String name);

    List<City> findAll();

    void deleteById(Long id);
}
