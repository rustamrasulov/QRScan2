package com.miirrr.qrscan.views;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.miirrr.qrscan.config.Config;
import com.miirrr.qrscan.views.tables.PositionTable;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Locale;

public class PositionMenu {
    private JPanel rootPanel;
    private JButton closeButton;

    private static final Config config = Config.getConfig();

    public PositionMenu(LocalDateTime dateFrom, LocalDateTime dateTo, Long shopId) {
        JDialog mainFrame = new JDialog();
        mainFrame.setIconImage(config.getLogoImage());
        mainFrame.setModal(true);
        mainFrame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        $$$setupUI$$$(dateFrom, dateTo, shopId);
        closeButton.addActionListener(e -> mainFrame.dispose());
        mainFrame.setResizable(false);
        mainFrame.add(rootPanel);
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$(LocalDateTime dateFrom, LocalDateTime dateTo, Long shopId) {
        createUIComponents();
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.setMaximumSize(new Dimension(1000, 700));
        rootPanel.setPreferredSize(new Dimension(1000, 700));
        rootPanel.setMinimumSize(new Dimension(1000, 700));

        JScrollPane positionTablePane = new JScrollPane();
        positionTablePane.setAlignmentX(0.5f);
        positionTablePane.setAlignmentY(0.5f);
        positionTablePane.setAutoscrolls(false);
        positionTablePane.setFocusable(false);
        positionTablePane.getVerticalScrollBar().setPreferredSize(new Dimension(60, 0));
        positionTablePane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return getScrollBarButton("↓");
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return getScrollBarButton("↑");
            }
        });

        Font positionTablePaneFont = this.$$$getFont$$$(null, -1, 36, positionTablePane.getFont());
        if (positionTablePaneFont != null) positionTablePane.setFont(positionTablePaneFont);
        rootPanel.add(positionTablePane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));

        JTable positionTable = PositionTable.getInstance(dateFrom, dateTo, shopId);
        positionTablePane.setViewportView(positionTable);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPanel.add(bottomPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        closeButton = new JButton();
        Font closeButtonFont = this.$$$getFont$$$(null, Font.BOLD, 24, closeButton.getFont());
        if (closeButtonFont != null) closeButton.setFont(closeButtonFont);
        closeButton.setText("ЗАКРЫТЬ");
        bottomPanel.add(closeButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 56), new Dimension(-1, 56), new Dimension(-1, 56), 0, false));
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
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
