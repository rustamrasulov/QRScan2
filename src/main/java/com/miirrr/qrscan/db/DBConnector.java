package com.miirrr.qrscan.db;

import com.miirrr.qrscan.config.Config;
import com.miirrr.qrscan.entities.City;
import com.miirrr.qrscan.entities.Position;
import com.miirrr.qrscan.entities.Shop;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

/**
 * Фабрика Hibernate-сессий и точка инициализации файловой H2-базы.
 */
public class DBConnector {

    private static SessionFactory sessionFactory;

    private static final Config config = Config.getConfig();

    /**
     * Возвращает singleton {@link SessionFactory}, создавая его при первом обращении.
     *
     * @return фабрика Hibernate-сессий
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = getConfiguration();

                configuration.addAnnotatedClass(City.class);
                configuration.addAnnotatedClass(Shop.class);
                configuration.addAnnotatedClass(Position.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                ensurePositionQrCodeUniqueIndex();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    /**
     * Закрывает текущую {@link SessionFactory} и очищает кеш singleton-соединения.
     * Используется интеграционными тестами для изоляции H2-баз между сценариями.
     */
    public static void resetSessionFactory() {
        if (sessionFactory != null) {
            try {
                sessionFactory.close();
            } finally {
                sessionFactory = null;
            }
        }
    }

    /**
     * Формирует Hibernate-конфигурацию на основе локального файла настроек.
     *
     * @return объект конфигурации Hibernate
     */
    private static Configuration getConfiguration() {
        Configuration configuration = new Configuration();

        Properties settings = new Properties();
        settings.put(Environment.DRIVER, "org.h2.Driver");
        settings.put(Environment.URL, "jdbc:h2:file:" + config.getDbPath() + "QRScan;DB_CLOSE_DELAY=-1");
        settings.put(Environment.USER, "sa");
        settings.put(Environment.PASS, "");
        settings.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");

        settings.put(Environment.SHOW_SQL, "false");

        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

        settings.put(Environment.HBM2DDL_AUTO, "update");

        configuration.setProperties(settings);
        return configuration;
    }

    /**
     * Гарантирует наличие уникального индекса по {@code qr_code}.
     * Нужен для существующих баз, где индекс мог быть снят более ранней миграцией.
     */
    private static void ensurePositionQrCodeUniqueIndex() {
        String jdbcUrl = "jdbc:h2:file:" + config.getDbPath() + "QRScan;DB_CLOSE_DELAY=-1";
        try (Connection connection = DriverManager.getConnection(jdbcUrl, "sa", "");
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE UNIQUE INDEX IF NOT EXISTS ux_position_qr_code ON position(qr_code)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
