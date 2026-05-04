package com.miirrr.qrscan.mockserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Локальный HTTP mock-сервер для интеграционного тестирования и локального запуска приложения.
 * Совместим с контрактом {@code WebServiceImpl}, который выполняет POST-запросы
 * в формате {@code {dbUrl}/{method}}.
 */
public final class MockWebServerModule implements AutoCloseable {

    private final HttpServer server;
    private final AtomicReference<String> shopsListResponse;
    private final AtomicReference<String> lastRequestBody;

    private MockWebServerModule(HttpServer server, String shopsListResponse) {
        this.server = server;
        this.shopsListResponse = new AtomicReference<>(Objects.requireNonNull(shopsListResponse, "shopsListResponse"));
        this.lastRequestBody = new AtomicReference<>("");
        this.server.createContext("/getshopslist", new JsonHandler(this.shopsListResponse, this.lastRequestBody));
        this.server.setExecutor(Executors.newCachedThreadPool());
    }

    /**
     * Поднимает mock-сервер на случайном свободном порту.
     *
     * @return запущенный модуль mock-сервера
     * @throws IOException если порт не удалось открыть
     */
    public static MockWebServerModule start() throws IOException {
        return start(MockResponses.defaultShopsList());
    }

    /**
     * Поднимает mock-сервер на случайном свободном порту
     * с пользовательским ответом для {@code /getshopslist}.
     *
     * @param shopsListResponse JSON-ответ для метода {@code getshopslist}
     * @return запущенный модуль mock-сервера
     * @throws IOException если порт не удалось открыть
     */
    public static MockWebServerModule start(String shopsListResponse) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        MockWebServerModule module = new MockWebServerModule(server, shopsListResponse);
        module.server.start();
        return module;
    }

    /**
     * Возвращает базовый URL, который должен быть подставлен в {@code dbUrl}.
     *
     * @return базовый адрес mock-сервера
     */
    public String getBaseUrl() {
        return "http://127.0.0.1:" + server.getAddress().getPort();
    }

    /**
     * Обновляет ответ, который будет возвращен на вызов {@code /getshopslist}.
     *
     * @param response JSON-ответ
     */
    public void setShopsListResponse(String response) {
        shopsListResponse.set(Objects.requireNonNull(response, "response"));
    }

    /**
     * Возвращает тело последнего запроса к {@code /getshopslist}.
     *
     * @return тело последнего HTTP-запроса
     */
    public String getLastRequestBody() {
        return lastRequestBody.get();
    }

    /**
     * Останавливает mock-сервер.
     */
    @Override
    public void close() {
        server.stop(0);
    }

    private static final class JsonHandler implements HttpHandler {

        private final AtomicReference<String> responseRef;
        private final AtomicReference<String> lastRequestBody;

        private JsonHandler(AtomicReference<String> responseRef, AtomicReference<String> lastRequestBody) {
            this.responseRef = responseRef;
            this.lastRequestBody = lastRequestBody;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                    exchange.sendResponseHeaders(405, -1);
                    return;
                }

                lastRequestBody.set(readRequestBody(exchange.getRequestBody()));

                byte[] response = responseRef.get().getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
                exchange.sendResponseHeaders(200, response.length);
                exchange.getResponseBody().write(response);
            } finally {
                exchange.close();
            }
        }

        private String readRequestBody(InputStream inputStream) throws IOException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toString(StandardCharsets.UTF_8.name());
        }
    }
}
