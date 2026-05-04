package com.miirrr.qrscan.views.tables;

import com.miirrr.qrscan.services.entities.PositionService;
import com.miirrr.qrscan.services.entities.PositionServiceImpl;
import com.miirrr.qrscan.entities.Position;
import com.miirrr.qrscan.entities.ProductType;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PositionTable extends JTable {

    private static final DefaultTableModel tableModel = PositionTableModel.getInstance();

    private static final PositionService positionService = new PositionServiceImpl();

    private static JTable INSTANCE;

    public static JTable getInstance(LocalDateTime startDate, LocalDateTime endDate) {
        return createTable(makeInstance(), startDate, endDate, null);
    }

    public static JTable getInstance(LocalDateTime startDate, LocalDateTime endDate, String ipInn) {
        return createTable(makeInstance(), startDate, endDate, ipInn, null);
    }

    public static JTable getInstance(LocalDateTime startDate, LocalDateTime endDate, Long storeId) {
        return createTable(makeInstance(), startDate, endDate, storeId, null);
    }

    public static JTable getInstance(LocalDateTime startDate, LocalDateTime endDate, ProductType productType) {
        return createTable(makeInstance(), startDate, endDate, productType);
    }

    public static JTable getInstance(LocalDateTime startDate, LocalDateTime endDate, String ipInn, ProductType productType) {
        return createTable(makeInstance(), startDate, endDate, ipInn, productType);
    }

    public static JTable getInstance(LocalDateTime startDate, LocalDateTime endDate, Long storeId, ProductType productType) {
        return createTable(makeInstance(), startDate, endDate, storeId, productType);
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
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        INSTANCE.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        INSTANCE.getColumnModel().getColumn(2).setMinWidth(220);
        INSTANCE.getColumnModel().getColumn(2).setMaxWidth(220);

        JTableHeader header = INSTANCE.getTableHeader();
        header.setPreferredSize(new Dimension(-1, 30));

        INSTANCE.setFocusable(false);

        return INSTANCE;
    }

    private static JTable createTable(JTable table, LocalDateTime startDate, LocalDateTime endDate, Long storeId, ProductType productType) {
        tableModel.setRowCount(0);

        getPositions(startDate, endDate, storeId, productType).forEach(p ->
                tableModel.addRow(new Object[]{p.getId(), p.getName(), getProductTypeDisplayName(p)}));

        table.clearSelection();
        table.repaint();

        return table;
    }

    private static JTable createTable(JTable table, LocalDateTime dateFrom, LocalDateTime dateTo, String ipInn, ProductType productType) {
        tableModel.setRowCount(0);

        getPositions(dateFrom, dateTo, ipInn, productType).forEach(p ->
                tableModel.addRow(new Object[]{p.getId(), p.getName(), getProductTypeDisplayName(p)}));

        table.clearSelection();
        table.repaint();

        return table;
    }

    private static JTable createTable(JTable table, LocalDateTime dateFrom, LocalDateTime dateTo, ProductType productType) {
        tableModel.setRowCount(0);

        getPositions(dateFrom, dateTo, productType).forEach(p ->
                tableModel.addRow(new Object[]{p.getId(), p.getName(), getProductTypeDisplayName(p)}));

        table.clearSelection();
        table.repaint();

        return table;
    }

    private static List<Position> getPositions(LocalDateTime dateFrom, LocalDateTime dateTo, ProductType productType) {
        return mergeByProductType(
                productType == null ? positionService.findByDate(dateFrom, dateTo, ProductType.FISH) : positionService.findByDate(dateFrom, dateTo, productType),
                productType == null ? positionService.findByDate(dateFrom, dateTo, ProductType.DRINKS) : null
        );
    }

    private static List<Position> getPositions(LocalDateTime dateFrom, LocalDateTime dateTo, Long shopId, ProductType productType) {
        return mergeByProductType(
                productType == null ? positionService.findByDateAndShopId(dateFrom, dateTo, shopId, ProductType.FISH) : positionService.findByDateAndShopId(dateFrom, dateTo, shopId, productType),
                productType == null ? positionService.findByDateAndShopId(dateFrom, dateTo, shopId, ProductType.DRINKS) : null
        );
    }

    private static List<Position> getPositions(LocalDateTime dateFrom, LocalDateTime dateTo, String ipInn, ProductType productType) {
        return mergeByProductType(
                productType == null ? positionService.findByDateAndShopINN(dateFrom, dateTo, ipInn, ProductType.FISH) : positionService.findByDateAndShopINN(dateFrom, dateTo, ipInn, productType),
                productType == null ? positionService.findByDateAndShopINN(dateFrom, dateTo, ipInn, ProductType.DRINKS) : null
        );
    }

    private static List<Position> mergeByProductType(List<Position> first, List<Position> second) {
        List<Position> result = new ArrayList<>();
        if (first != null) {
            result.addAll(first);
        }
        if (second != null) {
            result.addAll(second);
        }
        result.sort(Comparator.comparing(Position::getName, String.CASE_INSENSITIVE_ORDER));
        return result;
    }

    private static String getProductTypeDisplayName(Position position) {
        return position.getProductType() == null ? ProductType.FISH.getDisplayName() : position.getProductType().getDisplayName();
    }
}
