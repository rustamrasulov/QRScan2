package com.miirrr.qrscan.views;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.miirrr.qrscan.config.Config;
import com.miirrr.qrscan.entities.Shop;
import com.miirrr.qrscan.services.entities.PositionService;
import com.miirrr.qrscan.services.entities.PositionServiceImpl;
import com.miirrr.qrscan.services.entities.ShopService;
import com.miirrr.qrscan.services.entities.ShopServiceImpl;
import com.miirrr.qrscan.views.tables.CityTable;
import com.miirrr.qrscan.views.tables.ShopTable;
import com.miirrr.qrscan.views.tables.ShopTableModel;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.TimerTask;


public class MainGUI {
    private JTextField qrcodeField;
    private JPanel rootPanel;
    private JButton exitButton;
    private JButton exportButton;
    private JScrollPane cityPane;
    private JScrollPane shopPane;
    private JPanel bottomPanel;
    private JTable cityTable;
    private JTable shopTable;

    private static final ShopService shopService = new ShopServiceImpl();

    private static final PositionService positionService = new PositionServiceImpl();

    private static final DefaultTableModel shopTableModel = ShopTableModel.getInstance();

    private static final Config config = Config.getConfig();

    public MainGUI() {
        JFrame mainFrame = new JFrame();
        mainFrame.setIconImage(config.getLogoImage());
        mainFrame.setMinimumSize(new Dimension(1000, 700));
        mainFrame.setLocationRelativeTo(null);
        qrcodeField.requestFocus();

        actionCityTable(cityTable);
        actionShopTable(shopTable);
        actionMainFrame(qrcodeField);
        actionExportButton(exportButton);

        exitButton.addActionListener(e -> System.exit(0));

        mainFrame.setResizable(false);
        mainFrame.add(rootPanel);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private void actionMainFrame(JTextField textField) {
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int selectedRow = shopTable.getSelectedRow();
                if ((e.getKeyCode() == 10) && (selectedRow > -1)) {
                    long shopId = Long.parseLong(String.valueOf(shopTable.getValueAt(shopTable.getSelectedRow(), 0)));
                    if (textField.getText().length() > 0) {
                        positionService.save(textField.getText(), shopId);

//                    shopTable.getSelectionModel().clearSelection();
                        showMessage(textField.getText(), shopService.findById(shopId).getName());
                        textField.setText("");
//                    textField.setEnabled(false);

                        shopTable.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
                        textField.requestFocus();

                        shopTable.setValueAt(countPositions(shopId), selectedRow, 2);
                    }
                }
            }
        });
    }


    private void showMessage(String qrCode, String shop) {
        int timeout_ms = 1500;//3 * 1000 mSec
        String stringStart = "<HTML><h1>";
        String stringEnd = "</h1></HTML>";
        String message = stringStart + shop + "<br/>" + qrCode + stringEnd;

        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(null, "УСПЕШНО");

        dialog.setModal(false);
        dialog.setVisible(true);
        new Timer(timeout_ms, e -> dialog.setVisible(false)).start();
    }

    private void actionCityTable(JTable table) {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() > -1) {
                qrcodeField.setEnabled(false);
                createShopTable(Long.parseLong(table.getValueAt(table.getSelectedRow(), 0).toString()));
            }
        });
    }

    private void actionShopTable(JTable table) {
        table.getSelectionModel().addListSelectionListener(e -> {
//            if (!e.getValueIsAdjusting() && table.getSelectedRow() > -1) {
            if (table.getSelectedRow() > -1) {
                qrcodeField.setEnabled(true);
                qrcodeField.requestFocus();
            }

        });

        table.addMouseListener(new MouseAdapter() {
            private int eventCnt = 0;
            final java.util.Timer timer = new java.util.Timer("doubleClickTimer", false);

            @Override
            public void mouseClicked(MouseEvent e) {
                eventCnt = e.getClickCount();
                if (e.getClickCount() == 1) {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (eventCnt > 1) {
                                new PositionMenu(
                                        null,
                                        null,
                                        Long.parseLong(String.valueOf(table.getValueAt(table.getSelectedRow(), 0))));
                            }
                            eventCnt = 0;
                        }
                    }, 400);
                }
            }
        });
    }

    private void actionExportButton(JButton button) {
        button.addActionListener(e -> new ReportMenu());
    }

    private void createShopTable(long id) {
        shopTableModel.setRowCount(0);

        List<Shop> shops;

        if (id > 0) {
            shops = shopService.findByCityId(id);
            ;
        } else {
            shops = shopService.findAll();
        }

        shops.forEach(s ->
                shopTableModel.addRow(new Object[]{s.getId(), s.getName(), countPositions(s.getId())}));

        shopTable.clearSelection();
        shopPane.getVerticalScrollBar().setValue(1);
        shopTable.repaint();
    }

    private Object countPositions(long shopId) {
        LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay();
        long count = positionService.findByDateAndShopId(startOfToday, startOfToday.plusDays(1), shopId).stream().count();
        return count > 0 ? count : "";
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.setMaximumSize(new Dimension(1000, 700));
        rootPanel.setMinimumSize(new Dimension(1000, 700));
        rootPanel.setPreferredSize(new Dimension(1000, 700));

        final JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(topPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        cityPane = new JScrollPane();
        cityPane.setAlignmentX(0.5f);
        cityPane.setAlignmentY(0.5f);
        cityPane.setAutoscrolls(false);
        cityPane.setFocusable(false);
        Font cityPaneFont = this.$$$getFont$$$(null, -1, 24, cityPane.getFont());
        if (cityPaneFont != null) cityPane.setFont(cityPaneFont);
        topPanel.add(cityPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(230, -1), new Dimension(230, -1), new Dimension(230, -1), 0, false));
        cityTable = CityTable.getInstance();
        cityPane.setViewportView(cityTable);
        shopPane = new JScrollPane();
        shopPane.setAlignmentX(0.5f);
        shopPane.setAlignmentY(0.5f);
        shopPane.setFocusable(false);
        shopPane.getVerticalScrollBar().setPreferredSize(new Dimension(60, 0));
        shopPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return getScrollBarButton("↓");
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return getScrollBarButton("↑");
            }
        });

        Font shopPaneFont = this.$$$getFont$$$(null, -1, 24, shopPane.getFont());
        if (shopPaneFont != null) shopPane.setFont(shopPaneFont);
        topPanel.add(shopPane, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(770, -1), new Dimension(770, -1), 0, false));
        shopTable = ShopTable.getInstance();
        shopPane.setViewportView(shopTable);

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(bottomPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 60), new Dimension(-1, 60), new Dimension(-1, 60), 0, true));

        exitButton = new JButton();
        exitButton.setFocusable(false);
        Font exitButtonFont = this.$$$getFont$$$(null, Font.BOLD, 24, exitButton.getFont());
        if (exitButtonFont != null) exitButton.setFont(exitButtonFont);
        exitButton.setLabel("ВЫХОД");
        exitButton.setText("ВЫХОД");
        bottomPanel.add(exitButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 56), new Dimension(-1, 56), new Dimension(-1, 56), 0, false));

        exportButton = new JButton();
        exportButton.setFocusable(false);
        Font exportButtonFont = this.$$$getFont$$$(null, Font.BOLD, 24, exportButton.getFont());
        if (exportButtonFont != null) exportButton.setFont(exitButtonFont);
        exportButton.setLabel("Экспорт");
        exportButton.setText("Экспорт");
        bottomPanel.add(exportButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 56), new Dimension(-1, 56), new Dimension(-1, 56), 0, false));

        qrcodeField = new JTextField();
        qrcodeField.setEnabled(false);
        Font qrcodeFieldFont = this.$$$getFont$$$(null, -1, 36, qrcodeField.getFont());
        if (qrcodeFieldFont != null) qrcodeField.setFont(qrcodeFieldFont);
        bottomPanel.add(qrcodeField, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    private JButton getScrollBarButton(String label) {
        JButton button = new JButton();
        button.setText(label);
        button.setFont($$$getFont$$$(null, Font.BOLD, 36, button.getFont()));
        button.setPreferredSize(new Dimension(0, 60));
        button.setMinimumSize(new Dimension(0, 60));
        button.setMaximumSize(new Dimension(0, 60));
        button.setSize(new Dimension(0, 60));
        button.setFocusable(false);
        return button;
    }


    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

}
