package com.miirrr.qrscan.services.engine;

import com.miirrr.qrscan.entities.ProductType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * DTO для подготовки данных экспорта по магазину.
 * Содержит идентификационные данные магазина и список позиций для выгрузки.
 */
@Data
@Getter
@Setter
public class ExportClass {
    private String inn;
    private String ipName;
    private String name;
    private ProductType productType;
    private Map<Long, String> positionNames;
}
