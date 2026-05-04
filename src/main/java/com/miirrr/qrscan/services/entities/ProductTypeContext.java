package com.miirrr.qrscan.services.entities;

import com.miirrr.qrscan.entities.ProductType;

/**
 * Глобальный контекст выбранного вида продукции.
 * Используется UI, сервисами и экспортом как текущее активное значение.
 */
public final class ProductTypeContext {

    private static volatile ProductType currentType = ProductType.FISH;

    private ProductTypeContext() {
    }

    /**
     * Возвращает текущий выбранный вид продукции.
     *
     * @return активный вид продукции
     */
    public static ProductType getCurrentType() {
        return currentType;
    }

    /**
     * Обновляет текущий вид продукции.
     * При передаче {@code null} используется значение по умолчанию {@link ProductType#FISH}.
     *
     * @param productType новый активный вид продукции
     */
    public static void setCurrentType(ProductType productType) {
        currentType = productType == null ? ProductType.FISH : productType;
    }
}
