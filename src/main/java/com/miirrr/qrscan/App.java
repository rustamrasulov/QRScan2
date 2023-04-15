package com.miirrr.qrscan;

import com.miirrr.qrscan.config.Config;
import com.miirrr.qrscan.services.engine.ParseShop;
import com.miirrr.qrscan.services.engine.ParseShopImpl;
import com.miirrr.qrscan.services.engine.ReportScheduler;
import com.miirrr.qrscan.services.engine.ReportSchedulerImpl;
import com.miirrr.qrscan.views.MainGUI;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;

@Log4j2
public class App extends JPanel {
    private static final ReportScheduler reportScheduler = ReportSchedulerImpl.getInstance();

    private static final ParseShop parseShop = new ParseShopImpl();

    private static final Config config = Config.getConfig();

    public static void main(String[] args) {

        log.info("Starting...");

        if (config.isScheduler()) {
            try {
                reportScheduler.starUp();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        parseShop.loadStores();


        new MainGUI();
    }
}