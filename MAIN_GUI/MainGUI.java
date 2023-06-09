package com.miirrr.qrscan.views;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.miirrr.qrscan.entities.City;
import com.miirrr.qrscan.services.entities.CityService;
import com.miirrr.qrscan.services.entities.CityServiceImpl;
import com.miirrr.qrscan.views.tables.CityTable;
import com.miirrr.qrscan.views.tables.CityTableModel;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainGUI {
    private JTextField qrcodeField;
    private JPanel rootPanel;
    private JButton exitButton;
    private JScrollPane cityPane;
    private JScrollPane positionPane;
    private JPanel buttonPanel;
    private JPanel bottomPanel;
    private JTable cityTable;
    private JTable positionTable;
    private JButton buttonUp;
    private JButton buttonDown;

    private final static CityService cityService = new CityServiceImpl();

    private final DefaultTableModel cityTableModel = CityTableModel.getInstance();

    public MainGUI() {
        JFrame mainFrame = new JFrame();
        qrcodeField.setText(cityService.findById(1L).getName());
        mainFrame.setResizable(false);
        mainFrame.add(rootPanel);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.pack();
        qrcodeField.requestFocus();

        buttonDown.setFocusable(false);
        buttonUp.setFocusable(false);


        buttonDown.addActionListener(e -> System.out.println("button1"));


//        createCityTable(cityTableModel, cityTable);
        createCityTable();

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        mainFrame.setVisible(true);

//        JFrame frame = new JFrame("Arrow Button Demo");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setLayout(new BorderLayout());
//        frame.add(new BasicArrowButton(BasicArrowButton.EAST), BorderLayout.EAST);
//        frame.add(new BasicArrowButton(BasicArrowButton.NORTH), BorderLayout.NORTH);
//        frame.add(new BasicArrowButton(BasicArrowButton.SOUTH), BorderLayout.SOUTH);
//        frame.add(new BasicArrowButton(BasicArrowButton.WEST), BorderLayout.WEST);
//        frame.pack();
//        frame.setVisible(true);
    }

    private void createCityTable() {
        List<Object[]> rows = new ArrayList<>();

        cityTable = CityTable.getInstance();


        for (City c : cityService.findAll()) {
            rows.add(new Object[]{c.getId(), c.getName()});
            System.out.println(c);
        }

        cityTableModel.setRowCount(0);

        for (Object[] row : rows) {
            cityTableModel.addRow(row);
        }

        cityTable.repaint();

        if (cityTable.getRowCount() > 0) {
            cityTable.changeSelection(0, 0, true, false);
        }
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
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        cityPane = new JScrollPane();
        cityPane.setAlignmentX(0.5f);
        cityPane.setAlignmentY(0.5f);
        cityPane.setAutoscrolls(false);
        cityPane.setFocusable(false);
        Font cityPaneFont = this.$$$getFont$$$(null, -1, 36, cityPane.getFont());
        if (cityPaneFont != null) cityPane.setFont(cityPaneFont);
        panel1.add(cityPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(300, -1), new Dimension(300, -1), new Dimension(300, -1), 0, false));
        cityTable = new JTable();
        cityPane.setViewportView(cityTable);
        positionPane = new JScrollPane();
        positionPane.setAlignmentX(0.5f);
        positionPane.setAlignmentY(0.5f);
        positionPane.setFocusable(false);
        Font positionPaneFont = this.$$$getFont$$$(null, -1, 36, positionPane.getFont());
        if (positionPaneFont != null) positionPane.setFont(positionPaneFont);
        panel1.add(positionPane, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(700, -1), new Dimension(700, -1), 0, false));
        positionTable = new JTable();
        positionPane.setViewportView(positionTable);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(buttonPanel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, true));
        buttonUp = new JButton();
        buttonUp.setAlignmentX(0.5f);
        Font buttonUpFont = this.$$$getFont$$$(null, Font.BOLD, 36, buttonUp.getFont());
        if (buttonUpFont != null) buttonUp.setFont(buttonUpFont);
        buttonUp.setInheritsPopupMenu(false);
        buttonUp.setLabel("↑");
        buttonUp.setText("↑");
        buttonPanel.add(buttonUp, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(60, 300), new Dimension(60, 300), new Dimension(60, 300), 0, false));
        buttonDown = new JButton();
        Font buttonDownFont = this.$$$getFont$$$(null, Font.BOLD, 36, buttonDown.getFont());
        if (buttonDownFont != null) buttonDown.setFont(buttonDownFont);
        buttonDown.setLabel("↓");
        buttonDown.setText("↓");
        buttonPanel.add(buttonDown, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(60, 300), new Dimension(60, 300), new Dimension(60, 300), 0, false));
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(bottomPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 60), new Dimension(-1, 60), new Dimension(-1, 60), 0, true));
        exitButton = new JButton();
        exitButton.setFocusable(false);
        Font exitButtonFont = this.$$$getFont$$$(null, Font.BOLD, 24, exitButton.getFont());
        if (exitButtonFont != null) exitButton.setFont(exitButtonFont);
        exitButton.setLabel("ВЫХОД");
        exitButton.setText("ВЫХОД");
        bottomPanel.add(exitButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 56), new Dimension(-1, 56), new Dimension(-1, 56), 0, false));
        qrcodeField = new JTextField();
        Font qrcodeFieldFont = this.$$$getFont$$$(null, -1, 36, qrcodeField.getFont());
        if (qrcodeFieldFont != null) qrcodeField.setFont(qrcodeFieldFont);
        bottomPanel.add(qrcodeField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
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
