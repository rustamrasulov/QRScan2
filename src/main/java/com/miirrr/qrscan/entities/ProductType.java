package com.miirrr.qrscan.entities;

/**
 * Вид продукции, в контексте которого выполняются сканирование,
 * отображение позиций и экспорт отчетов.
 */
public enum ProductType {
    FISH("Рыба", "fish"),
    DRINKS("Напитки", "drinks");

    private final String displayName;
    private final String fileToken;

    ProductType(String displayName, String fileToken) {
        this.displayName = displayName;
        this.fileToken = fileToken;
    }

    /**
     * Возвращает человекочитаемое имя для отображения в интерфейсе.
     *
     * @return название вида продукции на русском языке
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Возвращает короткий токен для имени экспортируемого файла.
     *
     * @return строковый идентификатор вида продукции
     */
    public String getFileToken() {
        return fileToken;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
