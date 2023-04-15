package com.miirrr.qrscan.views.tables;

import javax.swing.table.DefaultTableModel;

public class CityTableModel extends DefaultTableModel {

    private static final String[] cityTableColumns = {"ID", "Название"};

    private static DefaultTableModel INSTANCE;

    public static DefaultTableModel getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new DefaultTableModel(cityTableColumns, 0) {};
        }
        return INSTANCE;
    }

}
