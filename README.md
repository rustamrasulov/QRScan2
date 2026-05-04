# QRScan

Swing-приложение для учета отсканированных DataMatrix/QR-кодов по магазинам с хранением данных в H2 и экспортом в Excel.

## Возможности

- выбор города и магазина из загруженного списка;
- сканирование кодов в привязке к магазину;
- раздельный учет по видам продукции:
  - `Рыба`
  - `Напитки`
- просмотр позиций за день по магазину;
- ручной экспорт позиций в `.xls`;
- плановый экспорт по Quartz;
- автоматический экспорт при смене ИНН магазина.

## Как работает вид продукции

В приложении есть глобальный переключатель вида продукции.

- Выбор выполняется в главном окне до выбора магазина.
- Текущее значение отображается в заголовке окна.
- Сканирование, счетчики, просмотр позиций и экспорт работают в рамках выбранного вида продукции.
- В именах экспортируемых файлов используется суффикс:
  - `fish`
  - `drinks`

## Стек

- Java 8+
- Maven
- Swing
- Hibernate
- H2
- Apache POI
- Quartz
- Lombok

## Структура проекта

- [src/main/java/com/miirrr/qrscan/App.java](/home/r/nordbeer/QRScan2/src/main/java/com/miirrr/qrscan/App.java) - точка входа
- [src/main/java/com/miirrr/qrscan/views/MainGUI.java](/home/r/nordbeer/QRScan2/src/main/java/com/miirrr/qrscan/views/MainGUI.java) - главное окно
- [src/main/java/com/miirrr/qrscan/views/ReportMenu.java](/home/r/nordbeer/QRScan2/src/main/java/com/miirrr/qrscan/views/ReportMenu.java) - окно фильтрации и экспорта
- [src/main/java/com/miirrr/qrscan/services/engine/ReportExport.java](/home/r/nordbeer/QRScan2/src/main/java/com/miirrr/qrscan/services/engine/ReportExport.java) - Excel-экспорт
- [src/main/java/com/miirrr/qrscan/services/engine/ParseShopImpl.java](/home/r/nordbeer/QRScan2/src/main/java/com/miirrr/qrscan/services/engine/ParseShopImpl.java) - загрузка списка магазинов
- [src/main/java/com/miirrr/qrscan/services/entities/ProductTypeContext.java](/home/r/nordbeer/QRScan2/src/main/java/com/miirrr/qrscan/services/entities/ProductTypeContext.java) - текущий вид продукции
- [src/main/java/com/miirrr/qrscan/db/DBConnector.java](/home/r/nordbeer/QRScan2/src/main/java/com/miirrr/qrscan/db/DBConnector.java) - инициализация БД

## Конфигурация

Приложение ищет файл конфигурации в таком порядке:

1. `%USER_HOME%/QRScan/qrscan.cfg`
2. `./qrscan.cfg`

Пример текущего файла: [qrscan.cfg](/home/r/nordbeer/QRScan2/qrscan.cfg)

Основные параметры:

- `dbUrl` - URL HTTP-сервиса 1С для получения магазинов и других данных
- `dbPath` - каталог хранения файла H2
- `scheduler` - включение планировщика (`yes/true/1` или `no/false/0`)
- `schedulerCron` - Quartz cron-expression
- `outPath` - директория экспорта Excel-файлов
- `dataMatrixLength` - минимальная длина сканируемого кода

## Сборка

```bash
mvn clean package
```

Артефакт сборки:

- `target/QRScan-jar-with-dependencies.jar`

## Запуск

```bash
java -jar target/QRScan-jar-with-dependencies.jar
```

При старте приложение:

1. Инициализирует H2 и Hibernate.
2. При включенном `scheduler` запускает Quartz.
3. Загружает список магазинов.
4. Открывает главное окно.

## Локальный запуск с имитацией сервера

Для запуска приложения без реального сервиса 1С добавлены runtime launcher-ы:

- [MockWebServerLauncher.java](/home/r/nordbeer/QRScan2/src/main/java/com/miirrr/qrscan/mockserver/MockWebServerLauncher.java) - поднимает только mock-сервер
- [LocalAppWithMockServerLauncher.java](/home/r/nordbeer/QRScan2/src/main/java/com/miirrr/qrscan/mockserver/LocalAppWithMockServerLauncher.java) - поднимает mock-сервер и сразу запускает приложение против него

Запуск только mock-сервера:

```bash
java -cp target/QRScan-jar-with-dependencies.jar com.miirrr.qrscan.mockserver.MockWebServerLauncher
```

Запуск приложения против mock-сервера:

```bash
java -cp target/QRScan-jar-with-dependencies.jar com.miirrr.qrscan.mockserver.LocalAppWithMockServerLauncher
```

Что делает `LocalAppWithMockServerLauncher`:

- создает временный `user.home`;
- пишет временный `QRScan/qrscan.cfg`;
- направляет `dbUrl` на локальный mock-сервер;
- создает отдельные временные директории для H2 и экспорта;
- запускает обычный `App.main(...)`.

## Работа в интерфейсе

1. Выберите вид продукции.
2. Выберите город.
3. Выберите магазин.
4. Отсканируйте код в нижнее поле.
5. Для просмотра позиций магазина выполните двойной клик по строке магазина.
6. Для выгрузки нажмите `Экспорт`, задайте период и директорию сохранения.

## Экспорт

Ручной экспорт:

- `ВСЕ` - формирует отдельные файлы по магазинам за выбранный период;
- выбранный магазин - формирует файл только по нему.

Плановый экспорт:

- запускается Quartz-задачей;
- выгружает позиции за последнюю неделю;
- проходит отдельно по `fish` и `drinks`;
- после успешной выгрузки помечает позиции как `scheduled=true`.

Формат имен файлов зависит от сценария, но всегда включает:

- магазин или ИП/ИНН;
- дату;
- суффикс вида продукции `fish` или `drinks`.

## База данных

Используется файловая H2-база.

- таблицы создаются и обновляются Hibernate через `hbm2ddl=update`;
- позиции сохраняются с признаком `product_type`;
- старый уникальный индекс по `qr_code` снимается при старте, чтобы одинаковый код мог существовать в разных видах продукции.

## Замечания по разработке

- GUI частично сгенерирован IntelliJ UI Designer;
- для части сущностей и DTO используются Lombok-аннотации;
- проект ориентирован на Windows-пути в примере конфигурации, но сам код не ограничен Windows.

## Интеграционное тестирование

В проект добавлен test support модуль для имитации HTTP-сервера 1С.
Сами классы mock-сервера лежат в runtime-коде и могут использоваться и тестами, и локальным запуском:

- [MockWebServerModule.java](/home/r/nordbeer/QRScan2/src/main/java/com/miirrr/qrscan/mockserver/MockWebServerModule.java) - локальный mock-сервер
- [MockResponses.java](/home/r/nordbeer/QRScan2/src/main/java/com/miirrr/qrscan/mockserver/MockResponses.java) - готовые JSON-ответы
- [IntegrationTestEnvironment.java](/home/r/nordbeer/QRScan2/src/test/java/com/miirrr/qrscan/testsupport/IntegrationTestEnvironment.java) - изолированное окружение с временным `qrscan.cfg`, H2 и mock-сервером

Типовой сценарий использования:

```java
import com.miirrr.qrscan.mockserver.MockResponses;

try (IntegrationTestEnvironment env = IntegrationTestEnvironment.create()) {
    env.getMockServer().setShopsListResponse(
        MockResponses.singleShop("k1", "Тестовый магазин", "1234567890", "127.0.0.1", "Сургут")
    );

    // далее вызывается код приложения, который читает Config и ходит в WebServiceImpl
}
```
