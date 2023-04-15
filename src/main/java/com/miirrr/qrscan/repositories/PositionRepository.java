package com.miirrr.qrscan.repositories;

import com.miirrr.qrscan.entities.Position;

import java.time.LocalDateTime;
import java.util.List;

public interface PositionRepository extends BaseRepository<Position>{


    List<Position> getByDate(LocalDateTime dateFrom, LocalDateTime dateTo);
    List<Position> getByDateAndShopINN(LocalDateTime dateFrom, LocalDateTime dateTo, String inn);

    List<Position> getByDateAndShopId(LocalDateTime dateFrom, LocalDateTime dateTo, long shopId);

}
