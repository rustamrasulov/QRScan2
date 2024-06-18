package com.miirrr.qrscan.services.entities;

import com.miirrr.qrscan.entities.Position;
import com.miirrr.qrscan.entities.Shop;
import com.miirrr.qrscan.repositories.PositionRepository;
import com.miirrr.qrscan.repositories.PositionRepositoryImpl;
import java.time.LocalDateTime;
import java.util.List;

public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository = new PositionRepositoryImpl();

    private final ShopService shopService = new ShopServiceImpl();

    @Override
    public Position save(Position position) {
        return positionRepository.save(position);
    }

    @Override
    public void save(String qrCode, long shopId) {
        Shop shopNew = shopService.findById(shopId);
        Position position;

        if (!existsByName(qrCode)) {
            position = new Position(qrCode, shopNew, LocalDateTime.now());
        } else {
            position = findByName(qrCode);
            position.setDate(LocalDateTime.now());
        }
        position.setShop(shopNew);
        save(position);
    }

    @Override
    public void saveAll(List<Position> positions) {
        positionRepository.saveAll(positions);
    }

    @Override
    public void update(Position position) {
        positionRepository.save(position);
    }

    @Override
    public Position findById(Long id) {
        return positionRepository.findById(id);
    }

    @Override
    public Position findByName(String name) {
        return positionRepository.findByName(name);
    }

    @Override
    public List<Position> findAll() {
        return positionRepository.findAll();
    }

    @Override
    public List<Position> findByDateAndShopId(LocalDateTime dateFrom, LocalDateTime dateTo, Long shopId) {
        LocalDateTime xDateFrom;
        LocalDateTime xDateTo;

        if (dateFrom == null || dateTo == null) {
            xDateFrom = LocalDateTime.now().toLocalDate().atStartOfDay();
            xDateTo = xDateFrom.plusDays(1).toLocalDate().atStartOfDay();
        } else {
            xDateFrom = dateFrom.toLocalDate().atStartOfDay();
            xDateTo = dateTo.plusDays(1).toLocalDate().atStartOfDay();
        }

        if (shopId == null) {
            return positionRepository.getByDate(xDateFrom, xDateTo);
        } else {
            return positionRepository.getByDateAndShopId(xDateFrom, xDateTo, shopId);
        }
    }

    @Override
    public List<Position> findByDate(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return positionRepository.getByDate(dateFrom, dateTo);
    }

    @Override
    public List<Position> findByDateAndShopINN(LocalDateTime dateFrom, LocalDateTime dateTo, String inn) {
        return positionRepository.getByDateAndShopINN(dateFrom, dateTo, inn);
    }

    @Override
    public void deleteById(Long id) {
        positionRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String qrCode) {
        return positionRepository.existsByName(qrCode);
    }

    @Override
    public boolean existsByNameAndShopId(String name, Long shopId) {
        return positionRepository.existsByNameAndShopId(name, shopId);
    }
}
