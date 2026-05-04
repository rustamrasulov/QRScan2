package com.miirrr.qrscan.services.entities;

import com.miirrr.qrscan.entities.Position;
import com.miirrr.qrscan.entities.ProductType;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис работы с отсканированными позициями.
 * Отвечает за сохранение, поиск, фильтрацию и удаление позиций.
 */
public interface PositionService {

    /**
     * Сохраняет сущность позиции.
     *
     * @param position позиция для сохранения
     * @return сохраненная позиция
     */
    Position save(Position position);

    /**
     * Сохраняет QR-код в рамках текущего выбранного вида продукции.
     *
     * @param qrCode отсканированный код
     * @param storeId идентификатор магазина
     */
    void save(String qrCode, long storeId);

    /**
     * Сохраняет QR-код в рамках указанного вида продукции.
     *
     * @param qrCode отсканированный код
     * @param storeId идентификатор магазина
     * @param productType вид продукции
     */
    void save(String qrCode, long storeId, ProductType productType);

    /**
     * Сохраняет набор позиций.
     *
     * @param positions список позиций
     */
    void saveAll(List<Position> positions);

    /**
     * Обновляет существующую позицию.
     *
     * @param position позиция для обновления
     */
    void update(Position position);

    /**
     * Ищет позицию по идентификатору.
     *
     * @param id идентификатор позиции
     * @return найденная позиция или {@code null}
     */
    Position findById(Long id);

    /**
     * Ищет позицию по коду в рамках текущего вида продукции.
     *
     * @param name QR-код
     * @return найденная позиция или {@code null}
     */
    Position findByName(String name);

    /**
     * Возвращает все позиции без дополнительной фильтрации.
     *
     * @return список всех позиций
     */
    List<Position> findAll();

    /**
     * Возвращает позиции магазина за период в рамках текущего вида продукции.
     *
     * @param dateFrom начало периода
     * @param dateTo конец периода
     * @param shopId идентификатор магазина
     * @return список найденных позиций
     */
    List<Position> findByDateAndShopId(LocalDateTime dateFrom, LocalDateTime dateTo, Long shopId);

    /**
     * Возвращает позиции магазина за период и для указанного вида продукции.
     *
     * @param dateFrom начало периода
     * @param dateTo конец периода
     * @param shopId идентификатор магазина
     * @param productType вид продукции
     * @return список найденных позиций
     */
    List<Position> findByDateAndShopId(LocalDateTime dateFrom, LocalDateTime dateTo, Long shopId, ProductType productType);

    /**
     * Возвращает позиции за период в рамках текущего вида продукции.
     *
     * @param dateFrom начало периода
     * @param dateTo конец периода
     * @return список найденных позиций
     */
    List<Position> findByDate(LocalDateTime dateFrom, LocalDateTime dateTo);

    /**
     * Возвращает позиции за период и для указанного вида продукции.
     *
     * @param dateFrom начало периода
     * @param dateTo конец периода
     * @param productType вид продукции
     * @return список найденных позиций
     */
    List<Position> findByDate(LocalDateTime dateFrom, LocalDateTime dateTo, ProductType productType);

    /**
     * Возвращает позиции по ИНН магазина за период в рамках текущего вида продукции.
     *
     * @param dateFrom начало периода
     * @param dateTo конец периода
     * @param inn ИНН магазина
     * @return список найденных позиций
     */
    List<Position> findByDateAndShopINN(LocalDateTime dateFrom, LocalDateTime dateTo, String inn);

    /**
     * Возвращает позиции по ИНН магазина за период и для указанного вида продукции.
     *
     * @param dateFrom начало периода
     * @param dateTo конец периода
     * @param inn ИНН магазина
     * @param productType вид продукции
     * @return список найденных позиций
     */
    List<Position> findByDateAndShopINN(LocalDateTime dateFrom, LocalDateTime dateTo, String inn, ProductType productType);

    /**
     * Удаляет позицию по идентификатору.
     *
     * @param id идентификатор позиции
     */
    void deleteById(Long id);

    /**
     * Проверяет существование QR-кода в рамках текущего вида продукции.
     *
     * @param qrCode QR-код
     * @return {@code true}, если позиция уже существует
     */
    boolean existsByName(String qrCode);

    /**
     * Проверяет существование QR-кода в рамках указанного вида продукции.
     *
     * @param qrCode QR-код
     * @param productType вид продукции
     * @return {@code true}, если позиция уже существует
     */
    boolean existsByName(String qrCode, ProductType productType);

    /**
     * Проверяет существование QR-кода для магазина в рамках текущего вида продукции.
     *
     * @param name QR-код
     * @param shopId идентификатор магазина
     * @return {@code true}, если позиция уже существует
     */
    boolean existsByNameAndShopId(String name, Long shopId);

    /**
     * Проверяет существование QR-кода для магазина и указанного вида продукции.
     *
     * @param name QR-код
     * @param shopId идентификатор магазина
     * @param productType вид продукции
     * @return {@code true}, если позиция уже существует
     */
    boolean existsByNameAndShopId(String name, Long shopId, ProductType productType);
}
