package com.miirrr.qrscan.views.tables;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShopTable extends JTable {

    private static final DefaultTableModel tableModel = ShopTableModel.getInstance();

    private static JTable INSTANCE;

    public static JTable getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JTable(tableModel) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        }

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(INSTANCE.getModel());

        INSTANCE.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));

        sorter.setSortKeys(sortKeys);

        INSTANCE.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        INSTANCE.setRowHeight(60);
        INSTANCE.setFont(new Font("", Font.BOLD, 28));
        INSTANCE.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        INSTANCE.getColumnModel().getColumn(0).setMinWidth(0);
        INSTANCE.getColumnModel().getColumn(0).setMaxWidth(0);
        INSTANCE.getColumnModel().getColumn(2).setMinWidth(60);
        INSTANCE.getColumnModel().getColumn(2).setMaxWidth(60);

        JTableHeader header = INSTANCE.getTableHeader();
        header.setPreferredSize(new Dimension(-1, 30));

        INSTANCE.setFocusable(false);

        return INSTANCE;
    }



}
