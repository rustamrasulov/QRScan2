package com.miirrr.qrscan.services.entities;

import com.miirrr.qrscan.entities.Position;
import com.miirrr.qrscan.entities.ProductType;
import com.miirrr.qrscan.entities.Shop;
import com.miirrr.qrscan.repositories.PositionRepository;
import com.miirrr.qrscan.repositories.PositionRepositoryImpl;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Реализация сервиса позиций поверх {@link PositionRepository}.
 * Поддерживает работу как с явным видом продукции, так и через текущий контекст.
 */
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository = new PositionRepositoryImpl();

    private final ShopService shopService = new ShopServiceImpl();

    /**
     * {@inheritDoc}
     */
    @Override
    public Position save(Position position) {
        return positionRepository.save(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(String qrCode, long shopId) {
        save(qrCode, shopId, ProductTypeContext.getCurrentType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(String qrCode, long shopId, ProductType productType) {
        Shop shopNew = shopService.findById(shopId);
        Position position = positionRepository.findByName(qrCode);

        if (position == null) {
            position = new Position(qrCode, shopNew, LocalDateTime.now(), productType);
        } else {
            position.setDate(LocalDateTime.now());
        }
        position.setShop(shopNew);
        position.setProductType(productType);
        save(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAll(List<Position> positions) {
        positionRepository.saveAll(positions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Position position) {
        positionRepository.save(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Position findById(Long id) {
        return positionRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Position findByName(String name) {
        return positionRepository.findByName(name);
    }

    /**
     * Ищет позицию по QR-коду и явному виду продукции.
     *
     * @param name QR-код
     * @param productType вид продукции
     * @return найденная позиция или {@code null}
     */
    public Position findByName(String name, ProductType productType) {
        return positionRepository.findByName(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Position> findAll() {
        return positionRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Position> findByDateAndShopId(LocalDateTime dateFrom, LocalDateTime dateTo, Long shopId) {
        return findByDateAndShopId(dateFrom, dateTo, shopId, ProductTypeContext.getCurrentType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Position> findByDateAndShopId(LocalDateTime dateFrom, LocalDateTime dateTo, Long shopId, ProductType productType) {
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
            return positionRepository.getByDate(xDateFrom, xDateTo, productType);
        } else {
            return positionRepository.getByDateAndShopId(xDateFrom, xDateTo, shopId, productType);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Position> findByDate(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return findByDate(dateFrom, dateTo, ProductTypeContext.getCurrentType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Position> findByDate(LocalDateTime dateFrom, LocalDateTime dateTo, ProductType productType) {
        return positionRepository.getByDate(dateFrom, dateTo, productType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Position> findByDateAndShopINN(LocalDateTime dateFrom, LocalDateTime dateTo, String inn) {
        return findByDateAndShopINN(dateFrom, dateTo, inn, ProductTypeContext.getCurrentType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Position> findByDateAndShopINN(LocalDateTime dateFrom, LocalDateTime dateTo, String inn, ProductType productType) {
        return positionRepository.getByDateAndShopINN(dateFrom, dateTo, inn, productType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long id) {
        positionRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByName(String qrCode) {
        return positionRepository.findByName(qrCode) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByName(String qrCode, ProductType productType) {
        return positionRepository.findByName(qrCode) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByNameAndShopId(String name, Long shopId) {
        return existsByNameAndShopId(name, shopId, ProductTypeContext.getCurrentType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByNameAndShopId(String name, Long shopId, ProductType productType) {
        return positionRepository.existsByNameAndShopId(name, shopId, productType);
    }
}
