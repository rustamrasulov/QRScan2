package com.miirrr.qrscan.views.tables;

import com.miirrr.qrscan.services.entities.PositionService;
import com.miirrr.qrscan.services.entities.PositionServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PositionTable extends JTable {

    private static final DefaultTableModel tableModel = PositionTableModel.getInstance();

    private static final PositionService positionService = new PositionServiceImpl();

    private static JTable INSTANCE;

    public static JTable getInstance(LocalDateTime startDate, LocalDateTime endDate) {
        return createTable(makeInstance(), startDate, endDate);
    }

    public static JTable getInstance(LocalDateTime startDate, LocalDateTime endDate, String ipInn) {
        return createTable(makeInstance(), startDate, endDate, ipInn);
    }

    public static JTable getInstance(LocalDateTime startDate, LocalDateTime endDate, Long storeId) {
        return createTable(makeInstance(), startDate, endDate, storeId);
    }

    private static JTable makeInstance() {
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
        INSTANCE.setFont(new Font("", Font.BOLD, 32));
        INSTANCE.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        INSTANCE.getColumnModel().getColumn(0).setMinWidth(0);
        INSTANCE.getColumnModel().getColumn(0).setMaxWidth(0);

        JTableHeader header = INSTANCE.getTableHeader();
        header.setPreferredSize(new Dimension(-1, 30));

        INSTANCE.setFocusable(false);

        return INSTANCE;
    }

    private static JTable createTable(JTable table, LocalDateTime startDate, LocalDateTime endDate, Long storeId) {
        tableModel.setRowCount(0);

        positionService.findByDateAndShopId(startDate, endDate, storeId).forEach(p ->
                tableModel.addRow(new Object[]{p.getId(), p.getName()}));

        table.clearSelection();
        table.repaint();

        return table;
    }

    private static JTable createTable(JTable table, LocalDateTime dateFrom, LocalDateTime dateTo, String ipInn) {
        tableModel.setRowCount(0);

        positionService.findByDateAndShopINN(dateFrom, dateTo, ipInn).forEach(p ->
                tableModel.addRow(new Object[]{p.getId(), p.getName()}));

        table.clearSelection();
        table.repaint();

        return table;
    }

    private static JTable createTable(JTable table, LocalDateTime dateFrom, LocalDateTime dateTo) {
        tableModel.setRowCount(0);

        positionService.findByDate(dateFrom, dateTo).forEach(p ->
                tableModel.addRow(new Object[]{p.getId(), p.getName()}));

        table.clearSelection();
        table.repaint();

        return table;
    }
}
