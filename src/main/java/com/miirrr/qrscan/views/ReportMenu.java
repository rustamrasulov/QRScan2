package com.miirrr.qrscan.views;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.miirrr.qrscan.config.Config;
import com.miirrr.qrscan.entities.Shop;
import com.miirrr.qrscan.services.engine.ReportExport;
import com.miirrr.qrscan.services.entities.ShopService;
import com.miirrr.qrscan.services.entities.ShopServiceImpl;
import com.miirrr.qrscan.views.tables.PositionTable;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXMonthView;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class ReportMenu {

    private final JDialog mainFrame;
    private JPanel rootPanel;
    private JTable positionTable;

    private JScrollPane positionTablePane;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JButton closeButton;
    private JButton filterButton;
    private JButton saveButton;

    private JComboBox<String> ipBox;
    private JXDatePicker pickerFrom;
    private JXDatePicker pickerTo;

    private DefaultComboBoxModel<String> defaultComboBoxIpModel;

    private static final ShopService shopService = new ShopServiceImpl();

    private static final Config config = Config.getConfig();


    public ReportMenu() {
        mainFrame = new JDialog();
        mainFrame.setMinimumSize(config.getSize());
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setIconImage(config.getLogoImage());
        mainFrame.setModal(true);
        mainFrame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        closeButton.addActionListener(e -> mainFrame.dispose());

        setIpBox();
        setFilterButtonAction(filterButton);
        setSaveButtonAction(saveButton);

        mainFrame.setResizable(false);
        mainFrame.getContentPane().add(rootPanel);
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);

    }

//    private void setIpBox() {
//        defaultComboBoxIpModel.addElement("ВСЕ");
//        List<String> toSort = new ArrayList<>();
//        Set<String> uniqueValues = new HashSet<>();
//        for (Shop s : shopService.findAll()) {
//            String ipName = s.getIpName() + " (" + s.getInn() + ")";
//            if (uniqueValues.add(ipName)) {
//                toSort.add(ipName);
//            }
//        }
//        toSort.sort(null);
//        for (String ipName : toSort) {
//            defaultComboBoxIpModel.addElement(ipName);
//        }
//    }

    private void setIpBox() {
        defaultComboBoxIpModel.addElement("ВСЕ");
        List<String> toSort = new ArrayList<>();
        Set<String> uniqueValues = new HashSet<>();
        for (Shop s : shopService.findAll()) {
            String shopName = s.getName();
            if (uniqueValues.add(shopName)) {
                toSort.add(shopName);
            }
        }
        toSort.sort(null);
        for (String shopName : toSort) {
            defaultComboBoxIpModel.addElement(shopName);
        }
    }

    private void setFilterButtonAction(JButton button) {
        button.addActionListener(e -> {
            LocalDateTime dateTimeFrom;
            LocalDateTime dateTimeTo;

            if (pickerFrom.getDate().equals(pickerTo.getDate())) {
                dateTimeFrom = pickerFrom.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                dateTimeTo = dateTimeFrom.plusDays(1);
            } else {
                dateTimeFrom = pickerFrom.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                dateTimeTo = pickerTo.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1);
            }

            if ((Objects.requireNonNull(ipBox.getSelectedItem())).toString().equals("ВСЕ")) {
                positionTable = PositionTable.getInstance(dateTimeFrom, dateTimeTo);
            } else {
//                positionTable = PositionTable.getInstance(dateTimeFrom, dateTimeTo, getIpBoxValue());
                positionTable = PositionTable.getInstance(dateTimeFrom, dateTimeTo, getIpBoxValue());
            }
        });
    }

    private long getIpBoxValue() {

        return shopService.findByName(String.valueOf(ipBox.getSelectedItem())).getId();

//        return Objects.requireNonNull(shopService.findAll().stream()
//                .filter(s -> s.getIpName().equals(Objects.requireNonNull(ipBox.getSelectedItem()).toString()))
//                .findFirst().orElse(null)).getInn();
    }

    private void setSaveButtonAction(JButton button) {
        button.addActionListener(e -> {
            String outPath;

            UIManager.put("FileChooser.cancelButtonText","Отмена");
            JFileChooser chooser = new JFileChooser(config.getOutPath()) {
                @Override
                public void setDialogType(int dialogType) {
                    super.setDialogType(dialogType);
                    setApproveButtonText("Сохранить");
                }
            };

            chooser.setDialogTitle("Выберите директорию");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            if (chooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {

                ReportExport reportExport = new ReportExport();

                LocalDateTime dateTimeFrom;
                LocalDateTime dateTimeTo;
                outPath = chooser.getSelectedFile().getAbsolutePath();

                if (pickerFrom.getDate().equals(pickerTo.getDate())) {
                    dateTimeFrom = pickerFrom.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    dateTimeTo = dateTimeFrom.plusDays(1);
                } else {
                    dateTimeFrom = pickerFrom.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    dateTimeTo = pickerTo.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(1);
                }
                if ((Objects.requireNonNull(ipBox.getSelectedItem())).toString().equals("ВСЕ")) {
                    reportExport.export(dateTimeFrom, dateTimeTo, null, outPath, false);
                    positionTable = PositionTable.getInstance(dateTimeFrom, dateTimeTo);
                } else {
                    reportExport.export(dateTimeFrom, dateTimeTo, getIpBoxValue(), outPath, false);
                    ipBox.setSelectedIndex(0);
                    positionTable = PositionTable.getInstance(dateTimeFrom, dateTimeTo);
                }
            }
        });
    }

    {
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
        createUIComponents();
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.setMaximumSize(config.getSize());
        rootPanel.setPreferredSize(config.getSize());
        rootPanel.setMinimumSize(config.getSize());

        topPanel = new JPanel();
        topPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(topPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        topPanel.add(boxPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        ipBox = new CustomComboBox<String>();
        ipBox.setPreferredSize(new Dimension(50, 50));
        ipBox.setBorder(LineBorder.createGrayLineBorder());
        Font cityBoxFont = this.$$$getFont$$$(null, -1, 32, ipBox.getFont());
        if (cityBoxFont != null) ipBox.setFont(cityBoxFont);
        defaultComboBoxIpModel = new DefaultComboBoxModel<>();
        ipBox.setModel(defaultComboBoxIpModel);
        boxPanel.add(ipBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        topPanel.add(datePanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        pickerFrom = new JXDatePicker();
        pickerTo = new JXDatePicker();
        pickerFrom.setDate(Calendar.getInstance().getTime());
        pickerFrom.setFormats(new SimpleDateFormat("dd.MM.yyyy"));
        pickerTo.setDate(Calendar.getInstance().getTime());
        pickerTo.setFormats(new SimpleDateFormat("dd.MM.yyyy"));
        Font pickerFont = this.$$$getFont$$$(null, -1, 24, pickerFrom.getFont());
        if (pickerFont != null) pickerFrom.setFont(pickerFont);
        if (pickerFont != null) pickerTo.setFont(pickerFont);
        datePanel.add(pickerFrom, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(280, -1), new Dimension(280, -1), new Dimension(280, -1), 0, false));
        datePanel.add(pickerTo, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(280, -1), new Dimension(280, -1), new Dimension(280, -1), 0, false));
        JXMonthView monthViewFrom = pickerFrom.getMonthView();
        JXMonthView monthViewTo = pickerTo.getMonthView();
        monthViewFrom.setFont(pickerFont);
        monthViewTo.setFont(pickerFont);
        filterButton = new JButton();
        Font filterButtonFont = this.$$$getFont$$$(null, Font.BOLD, 24, filterButton.getFont());
        if (filterButtonFont != null) filterButton.setFont(filterButtonFont);
        filterButton.setText("ПРИМЕНИТЬ");
        datePanel.add(filterButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(200, 56), new Dimension(200, 56), new Dimension(200, 56), 0, false));
        saveButton = new JButton();
        Font saveButtonFont = this.$$$getFont$$$(null, Font.BOLD, 24, saveButton.getFont());
        if (saveButtonFont != null) saveButton.setFont(saveButtonFont);
        saveButton.setText("СОХРАНИТЬ");
        datePanel.add(saveButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(200, 56), new Dimension(200, 56), new Dimension(200, 56), 0, false));
        positionTablePane = new JScrollPane();
        rootPanel.add(positionTablePane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        positionTable = PositionTable.getInstance(LocalDateTime.now().toLocalDate().atStartOfDay(), LocalDateTime.now().toLocalDate().atStartOfDay().plusDays(1));
        positionTablePane.setViewportView(positionTable);
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(bottomPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        closeButton = new JButton();
        Font closeButtonFont = this.$$$getFont$$$(null, Font.BOLD, 24, closeButton.getFont());
        if (closeButtonFont != null) closeButton.setFont(closeButtonFont);
        closeButton.setText("ЗАКРЫТЬ");
        bottomPanel.add(closeButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 56), new Dimension(-1, 56), new Dimension(-1, 56), 0, false));
    }

    private static class CustomComboBox<T> extends JComboBox<T> {
        public CustomComboBox() {
            super();
            init();
        }

        public void init() {
            CustomComboBoxUI ccbUi = new CustomComboBoxUI();
            setUI(ccbUi);
        }

    }

    private static class CustomComboBoxUI extends BasicComboBoxUI {
        protected ComboPopup createPopup() {
            return new CustomComboBoxPopup(comboBox);
        }

    }

    private static class CustomComboBoxPopup extends BasicComboPopup {
        public CustomComboBoxPopup(JComboBox<?> combo) {
            super((JComboBox<Object>) combo);
        }

        @Override
        protected void configureScroller() {
            super.configureScroller();
            scroller.getVerticalScrollBar().setPreferredSize(new Dimension(40, 0));
        }

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

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
