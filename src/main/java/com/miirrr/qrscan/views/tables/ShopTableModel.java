package com.miirrr.qrscan.views.tables;

import javax.swing.table.DefaultTableModel;

public class ShopTableModel extends DefaultTableModel {
    private static final String[] storeTableColumns = {"ID", "Название", "<html><center>QR<br/>Рыба/Напитки</center></html>"};

    private static DefaultTableModel INSTANCE;

    public static DefaultTableModel getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new DefaultTableModel(storeTableColumns, 0) {};
        }
        return INSTANCE;
    }
}
