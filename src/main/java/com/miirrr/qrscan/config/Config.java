package com.miirrr.qrscan.config;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class Config {


    private static volatile Config INSTANCE;

    private static String configPath;

    private Config() {
    }

    public static Config getConfig() {
        if (Files.exists(new File(Paths.get(System.getProperty("user.home")).toAbsolutePath().normalize() + "/QRScan/qrscan.cfg").toPath())) {
            configPath = Paths.get(System.getProperty("user.home")).toAbsolutePath().normalize() + "/QRScan/qrscan.cfg";
        } else {
            configPath = Paths.get(".").toAbsolutePath().normalize() + "/qrscan.cfg";
        }
        if (INSTANCE == null) {
            synchronized (Config.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Config();
                }
            }
        }
        return INSTANCE;
    }

    public Image getLogoImage() {
        URL logoURL = getClass().getResource("/images/logo.png");
        assert logoURL != null;
        return new ImageIcon(logoURL).getImage();
    }

    public String getDbUrl() {
        String dbUrl = getProperties().getProperty("dbUrl");

        if (!dbUrl.endsWith("/")) {
            return dbUrl + "/";
        }

        return dbUrl;
    }

    public String getDbPath() {
        String dbPath = getProperties().getProperty("dbPath");

        if(dbPath.length() == 0) {
            dbPath = Paths.get(System.getProperty("user.home")).toAbsolutePath().normalize() + "/QRScan/";
        } else {
            if (!dbPath.endsWith("/")) {
                return dbPath + "/";
            }
        }

        return dbPath;
    }

    public boolean isScheduler() {
        boolean isScheduled = false;
        String string = getProperties().getProperty("scheduler").toLowerCase();
        Set<String> trueSet = new HashSet<>(Arrays.asList("1", "true", "yes"));

        if (trueSet.contains(string)) {
            isScheduled = true;
        }

        return isScheduled;

    }

    public String getSchedulerCron() {
        return getProperties().getProperty("schedulerCron");
    }

    public String getOutPath() {
        return getProperties().getProperty("outPath").replaceAll("\\\\", "\\\\\\\\");
    }

    public Dimension getSize() {
        return new Dimension(1000, 640);
    }

    private Properties getProperties() {
        Properties properties = new Properties();

        try (InputStream inputStream = Files.newInputStream(Paths.get(configPath))) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }
}
