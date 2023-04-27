package com.miirrr.qrscan.services.engine;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Data
@Getter
@Setter
public class ExportClass {
    private String inn;
    private String ipName;
    private String name;
    private Map<Long, String> positionNames;
}