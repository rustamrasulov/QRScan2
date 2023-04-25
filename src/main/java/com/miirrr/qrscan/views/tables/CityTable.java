package com.miirrr.qrscan.views.tables;

import com.miirrr.qrscan.entities.City;
import com.miirrr.qrscan.services.entities.CityService;
import com.miirrr.qrscan.services.entities.CityServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CityTable extends JTable {

    private static final CityService cityService = new CityServiceImpl();

    private static final DefaultTableModel tableModel = CityTableModel.getInstance();
    private static JTable INSTANCE;

    public static JTable getInstance() {
        if(INSTANCE == null) {
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

        JTableHeader header = INSTANCE.getTableHeader();
        header.setPreferredSize(new Dimension(-1, 30));

        createCityTable(INSTANCE);

        INSTANCE.setFocusable(false);

        return INSTANCE;
    }

    private static void createCityTable(JTable table) {
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{0, "[ВСЕ]"});
        for (City c : cityService.findAll()) {
            tableModel.addRow(new Object[]{c.getId(), c.getName()});
        }

        table.clearSelection();

        table.repaint();
    }

}
