package com.miirrr.qrscan.services.entities;

import com.miirrr.qrscan.entities.Position;
import java.time.LocalDateTime;
import java.util.List;

public interface PositionService {

    Position save(Position position);

    void save(String qrCode, long storeId);

    void saveAll(List<Position> positions);

    void update(Position position);

    Position findById(Long id);

    Position findByName(String name);

    List<Position> findAll();

    List<Position> findByDateAndShopId(LocalDateTime dateFrom, LocalDateTime dateTo, Long shopId);

    List<Position> findByDate(LocalDateTime dateFrom, LocalDateTime dateTo);

    List<Position> findByDateAndShopINN(LocalDateTime dateFrom, LocalDateTime dateTo, String inn);

    void deleteById(Long id);

    boolean existsByName(String qrCode);

    boolean existsByNameAndShopId(String name, Long shopId);
}
