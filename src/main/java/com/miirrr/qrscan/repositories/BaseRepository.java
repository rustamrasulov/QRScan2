package com.miirrr.qrscan.repositories;

import com.miirrr.qrscan.entities.BaseEntity;

import java.util.List;

public interface BaseRepository<T extends BaseEntity> {
    T save(T entity);

    void saveAll(List<T> entities);

    T findById(Long id);

    T findByName(String name);

    List<T> findAll();

    void deleteById(Long id);
}
