package com.miirrr.qrscan.testsupport;

import com.miirrr.qrscan.config.Config;
import com.miirrr.qrscan.db.DBConnector;
import com.miirrr.qrscan.mockserver.MockWebServerModule;
import com.miirrr.qrscan.services.web.WebServiceImpl;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Подготавливает изолированное окружение для интеграционных тестов:
 * временный {@code qrscan.cfg}, отдельную H2-базу и локальный mock-сервер.
 */
public final class IntegrationTestEnvironment implements AutoCloseable {

    private final Path workDir;
    private final Path dbDir;
    private final Path outDir;
    private final Path userHomeDir;
    private final MockWebServerModule mockServer;

    private IntegrationTestEnvironment(Path workDir, Path dbDir, Path outDir, Path userHomeDir, MockWebServerModule mockServer) {
        this.workDir = workDir;
        this.dbDir = dbDir;
        this.outDir = outDir;
        this.userHomeDir = userHomeDir;
        this.mockServer = mockServer;
    }

    /**
     * Создает изолированное тестовое окружение с базовым ответом mock-сервера.
     *
     * @return подготовленное окружение
     * @throws IOException если не удалось создать временные директории или конфиг
     */
    public static IntegrationTestEnvironment create() throws IOException {
        Path workDir = Files.createTempDirectory("qrscan-it-");
        Path dbDir = Files.createDirectories(workDir.resolve("db"));
        Path outDir = Files.createDirectories(workDir.resolve("out"));
        Path userHomeDir = Files.createDirectories(workDir.resolve("home"));
        Path qrScanHome = Files.createDirectories(userHomeDir.resolve("QRScan"));

        MockWebServerModule mockServer = MockWebServerModule.start();

        String config = ""
            + "dbUrl=" + mockServer.getBaseUrl() + "\n"
            + "dbPath=" + dbDir.toAbsolutePath() + "\n"
            + "scheduler=no\n"
            + "schedulerCron=0/5 * * ? * *\n"
            + "outPath=" + outDir.toAbsolutePath() + "\n"
            + "dataMatrixLength=20\n";

        Files.write(qrScanHome.resolve("qrscan.cfg"), config.getBytes(StandardCharsets.UTF_8));
        System.setProperty("user.home", userHomeDir.toAbsolutePath().toString());

        resetApplicationState();

        return new IntegrationTestEnvironment(workDir, dbDir, outDir, userHomeDir, mockServer);
    }

    /**
     * Возвращает mock-сервер для подмены ответов и проверки запросов.
     *
     * @return модуль mock-сервера
     */
    public MockWebServerModule getMockServer() {
        return mockServer;
    }

    /**
     * Возвращает рабочую директорию окружения.
     *
     * @return путь к корневой временной директории
     */
    public Path getWorkDir() {
        return workDir;
    }

    /**
     * Сбрасывает состояние приложения между интеграционными сценариями.
     */
    public static void resetApplicationState() {
        resetApplicationStateStatic();
    }

    /**
     * Освобождает ресурсы mock-сервера и H2.
     */
    @Override
    public void close() {
        resetApplicationStateStatic();
        mockServer.close();
    }

    private static void resetApplicationStateStatic() {
        DBConnector.resetSessionFactory();
        WebServiceImpl.resetInstance();
        Config.reset();
    }
}
