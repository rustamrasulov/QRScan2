package com.miirrr.qrscan.views.tables;

import javax.swing.table.DefaultTableModel;

public class PositionTableModel {
    private static final String[] positionTableColumns = {"ID", "Название"};

    private static DefaultTableModel INSTANCE;

    public static DefaultTableModel getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new DefaultTableModel(positionTableColumns, 0) {};
        }
        return INSTANCE;
    }
}
