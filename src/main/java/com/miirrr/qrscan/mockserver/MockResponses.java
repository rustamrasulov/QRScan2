package com.miirrr.qrscan.mockserver;

/**
 * Набор готовых JSON-ответов для имитации HTTP-сервиса.
 */
public final class MockResponses {

    private MockResponses() {
    }

    /**
     * Возвращает базовый ответ со списком магазинов,
     * совместимый с {@code ParseShopImpl}.
     *
     * @return JSON-строка метода {@code getshopslist}
     */
    public static String defaultShopsList() {
        return "{\n" +
            "  \"shopslist\": [\n" +
            "    {\n" +
            "      \"id\": \"shop_id_1\",\n" +
            "      \"name\": \"Shop 1\",\n" +
            "      \"inn\": \"1234567890\",\n" +
            "      \"ip\": \"192.168.1.1\",\n" +
            "      \"city\": \"City 1\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"shop_id_2\",\n" +
            "      \"name\": \"Shop 2\",\n" +
            "      \"inn\": \"0987654321\",\n" +
            "      \"ip\": \"192.168.1.2\",\n" +
            "      \"city\": \"City 2\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"shop_id_3\",\n" +
            "      \"name\": \"Shop 3\",\n" +
            "      \"inn\": \"1357902468\",\n" +
            "      \"ip\": \"192.168.1.3\",\n" +
            "      \"city\": \"City 1\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    }

    /**
     * Формирует ответ с одним магазином.
     *
     * @param id внешний идентификатор магазина
     * @param name название магазина
     * @param inn ИНН
     * @param ip IP или имя ИП
     * @param city город
     * @return JSON-строка метода {@code getshopslist}
     */
    public static String singleShop(String id, String name, String inn, String ip, String city) {
        return "{\n" +
            "  \"shopslist\": [\n" +
            "    {\n" +
            "      \"id\": \"" + escape(id) + "\",\n" +
            "      \"name\": \"" + escape(name) + "\",\n" +
            "      \"inn\": \"" + escape(inn) + "\",\n" +
            "      \"ip\": \"" + escape(ip) + "\",\n" +
            "      \"city\": \"" + escape(city) + "\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
