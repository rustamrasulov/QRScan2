package com.miirrr.qrscan.services.entities;

import com.miirrr.qrscan.entities.City;
import com.miirrr.qrscan.repositories.CityRepository;
import com.miirrr.qrscan.repositories.CityRepositoryImpl;

import java.util.List;

public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository = new CityRepositoryImpl();

    @Override
    public City save(City city) {
        boolean isNew = true;
        for (City c : cityRepository.findAll()) {
            if (c.getName().equals(city.getName())) {
                isNew = false;
                break;
            }
        }
        return cityRepository.save(city);
    }

    @Override
    public void saveAll(List<City> cities) {
        cityRepository.saveAll(cities);
    }

    @Override
    public void update(City city) {
        cityRepository.save(city);
    }

    @Override
    public City findById(Long id) {
        return cityRepository.findById(id);
    }

    @Override
    public City findByName(String name) {
        return cityRepository.findByName(name);
    }

    public City getCity(String name) {
        City city = findByName(name);
        if (city == null) {
            city = new City(name);
            save(city);
        }
        return city;
    }


    @Override
    public List<City> findAll() {
        return cityRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        cityRepository.deleteById(id);
    }
}
