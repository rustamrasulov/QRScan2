package com.miirrr.qrscan.views;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.miirrr.qrscan.config.Config;
import com.miirrr.qrscan.services.entities.PositionService;
import com.miirrr.qrscan.services.entities.PositionServiceImpl;
import com.miirrr.qrscan.views.tables.PositionTable;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.TimerTask;

import static javax.swing.GroupLayout.Alignment.*;
import static javax.swing.GroupLayout.Alignment.BASELINE;

public class PositionMenu extends JFrame {
    private JPanel rootPanel;
    private JButton closeButton;

    private JTable positionTable;

    private static final Config config = Config.getConfig();

    private final PositionService positionService = new PositionServiceImpl();

    public PositionMenu(LocalDateTime dateFrom, LocalDateTime dateTo, Long shopId) {


        JDialog mainFrame = new JDialog();
        mainFrame.setMinimumSize(config.getSize());
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setIconImage(config.getLogoImage());
        mainFrame.setModal(true);
        mainFrame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        $$$setupUI$$$(dateFrom, dateTo, shopId);

        if (positionTable.getMouseListeners().length < 3) {
            positionTable.addMouseListener(new MouseAdapter() {
                private int eventCnt = 0;
                final java.util.Timer timer = new java.util.Timer("doubleClick", false);

                @Override
                public void mousePressed(MouseEvent e) {
                    eventCnt = e.getClickCount();
                    if (e.getClickCount() == 1) {
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (eventCnt > 1) {
                                    confirmationDialog(dateFrom, dateTo, shopId);
                                }
                                eventCnt = 0;
                            }
                        }, 400);
                    }
                }
            });
        }
        closeButton.addActionListener(e -> mainFrame.dispose());
        mainFrame.setResizable(false);
        mainFrame.add(rootPanel);
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private void confirmationDialog(LocalDateTime dateFrom, LocalDateTime dateTo, Long shopId) {

        // https://javaswing.wordpress.com/2010/04/05/jframe_close_confirm/
        // https://stackoverflow.com/questions/32051657/how-to-perform-action-after-jframe-is-closed
        // https://java-online.ru/swing-jtable.xhtml
        String stringStart = "<HTML><h1>";
        String stringEnd = "</h1></HTML>";
        String message = stringStart + "Подвердите удаление" + "<br/>" + positionTable.getValueAt(positionTable.getSelectedRow(), 1) + stringEnd;

        JDialog jDialog = new JDialog(this, "Подверждение", true);
        jDialog.setIconImage(config.getLogoImage());
        jDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        jDialog.setModal(true);
        jDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        jDialog.setSize(360, 200);
        jDialog.setBounds(360, 200, 360, 200);
        jDialog.setLocationRelativeTo(null);
        jDialog.setLayout(null);
        jDialog.setResizable(false);

        JLabel jLabel = new JLabel(message);
        jLabel.setFont(this.$$$getFont$$$(null, Font.PLAIN, 36, jLabel.getFont()));
        jLabel.setOpaque(true);
        jLabel.setMinimumSize(new Dimension(300, 50));

        jLabel.setHorizontalAlignment(JTextField.CENTER);

        JButton confirmButton = new JButton("УДАЛИТЬ");
        JButton cancelButton = new JButton("ОТМЕНА");

        List<JButton> buttons = Arrays.asList(confirmButton, cancelButton);

        for (JButton button : buttons) {
            button.setMinimumSize(new Dimension(140, 50));
            button.setMaximumSize(new Dimension(140, 50));
            button.setPreferredSize(new Dimension(140, 50));
            button.setSize(new Dimension(140, 50));
            button.setFocusable(false);
            button.setFont(this.$$$getFont$$$(null, Font.BOLD, 20, button.getFont()));
        }

        confirmButton.addActionListener(e -> {
            positionService.deleteById(Long.parseLong(String.valueOf(positionTable.getValueAt(positionTable.getSelectedRow(), 0))));
            positionTable = PositionTable.getInstance(dateFrom, dateTo, shopId);
            this.dispose();
        });

        cancelButton.addActionListener(e -> this.dispose());

        GroupLayout layout = new GroupLayout(jDialog.getContentPane());
        jDialog.getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup(CENTER)
                .addComponent(jLabel)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(CENTER)
                                .addComponent(confirmButton))
                        .addGroup(layout.createParallelGroup(CENTER)
                                .addComponent(cancelButton))));

        layout.linkSize(SwingConstants.VERTICAL, confirmButton, cancelButton);

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(jLabel))
                .addGroup(layout.createParallelGroup(LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(confirmButton)
                                        .addComponent(cancelButton)))));

        jDialog.pack();
        jDialog.setVisible(true);
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
        rootPanel.setMaximumSize(config.getSize());
        rootPanel.setPreferredSize(config.getSize());
        rootPanel.setMinimumSize(config.getSize());

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

        positionTable = PositionTable.getInstance(dateFrom, dateTo, shopId);
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
