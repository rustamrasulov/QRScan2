package com.miirrr.qrscan.mockserver;

import com.miirrr.qrscan.App;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Launcher для локального запуска приложения против встроенной имитации веб-сервера.
 * Создает временный {@code qrscan.cfg}, отдельную H2-базу и поднимает mock backend.
 */
public final class LocalAppWithMockServerLauncher {

    private LocalAppWithMockServerLauncher() {
    }

    /**
     * Запускает mock-сервер и приложение, направленное на этот mock.
     *
     * @param args аргументы командной строки не используются
     * @throws Exception если не удалось подготовить окружение или запустить приложение
     */
    public static void main(String[] args) throws Exception {
        Path workDir = Files.createTempDirectory("qrscan-local-mock-");
        Path dbDir = Files.createDirectories(workDir.resolve("db"));
        Path outDir = Files.createDirectories(workDir.resolve("out"));
        Path userHomeDir = Files.createDirectories(workDir.resolve("home"));
        Path qrScanHome = Files.createDirectories(userHomeDir.resolve("QRScan"));

        try (MockWebServerModule server = MockWebServerModule.start()) {
            String config = ""
                + "dbUrl=" + server.getBaseUrl() + "\n"
                + "dbPath=" + dbDir.toAbsolutePath() + "\n"
                + "scheduler=no\n"
                + "schedulerCron=0/5 * * ? * *\n"
                + "outPath=" + outDir.toAbsolutePath() + "\n"
                + "dataMatrixLength=20\n";

            Files.write(qrScanHome.resolve("qrscan.cfg"), config.getBytes(StandardCharsets.UTF_8));
            System.setProperty("user.home", userHomeDir.toAbsolutePath().toString());

            System.out.println("Mock server started at: " + server.getBaseUrl());
            System.out.println("Temp home: " + userHomeDir.toAbsolutePath());
            System.out.println("DB path: " + dbDir.toAbsolutePath());
            System.out.println("Export path: " + outDir.toAbsolutePath());

            App.main(new String[0]);
        }
    }
}
