package com.miirrr.qrscan.mockserver;

/**
 * Launcher для ручного запуска mock-сервера без приложения.
 */
public final class MockWebServerLauncher {

    private MockWebServerLauncher() {
    }

    /**
     * Поднимает mock-сервер и ожидает завершения процесса.
     *
     * @param args аргументы командной строки не используются
     * @throws Exception если сервер не удалось запустить
     */
    public static void main(String[] args) throws Exception {
        try (MockWebServerModule server = MockWebServerModule.start()) {
            System.out.println("Mock server started at: " + server.getBaseUrl());
            Thread.currentThread().join();
        }
    }
}
