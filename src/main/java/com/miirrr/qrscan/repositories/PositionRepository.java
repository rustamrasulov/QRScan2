package com.miirrr.qrscan.repositories;

import com.miirrr.qrscan.entities.ProductType;
import com.miirrr.qrscan.entities.Position;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий доступа к сущности {@link Position} с выборками,
 * зависящими от периода, магазина и вида продукции.
 */
public interface PositionRepository extends BaseRepository<Position>{

    /**
     * Возвращает позиции за период и вид продукции.
     *
     * @param dateFrom начало периода
     * @param dateTo конец периода
     * @param productType вид продукции
     * @return список позиций
     */
    List<Position> getByDate(LocalDateTime dateFrom, LocalDateTime dateTo, ProductType productType);

    /**
     * Возвращает позиции по ИНН магазина, периоду и виду продукции.
     *
     * @param dateFrom начало периода
     * @param dateTo конец периода
     * @param inn ИНН магазина
     * @param productType вид продукции
     * @return список позиций
     */
    List<Position> getByDateAndShopINN(LocalDateTime dateFrom, LocalDateTime dateTo, String inn, ProductType productType);

    /**
     * Возвращает позиции по идентификатору магазина, периоду и виду продукции.
     *
     * @param dateFrom начало периода
     * @param dateTo конец периода
     * @param shopId идентификатор магазина
     * @param productType вид продукции
     * @return список позиций
     */
    List<Position> getByDateAndShopId(LocalDateTime dateFrom, LocalDateTime dateTo, long shopId, ProductType productType);

    /**
     * Проверяет наличие позиции по QR-коду и виду продукции.
     *
     * @param name QR-код
     * @param productType вид продукции
     * @return {@code true}, если позиция существует
     */
    boolean existsByName(String name, ProductType productType);

    /**
     * Проверяет наличие позиции по QR-коду, магазину и виду продукции.
     *
     * @param name QR-код
     * @param shopId идентификатор магазина
     * @param productType вид продукции
     * @return {@code true}, если позиция существует
     */
    boolean existsByNameAndShopId(String name, Long shopId, ProductType productType);

    /**
     * Возвращает позицию по QR-коду и виду продукции.
     *
     * @param name QR-код
     * @param productType вид продукции
     * @return найденная позиция или {@code null}
     */
    Position findByName(String name, ProductType productType);
}
