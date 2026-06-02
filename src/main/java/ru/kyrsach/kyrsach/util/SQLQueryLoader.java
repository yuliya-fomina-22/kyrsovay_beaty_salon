package ru.kyrsach.kyrsach.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class SQLQueryLoader {

    private static final Logger logger = LoggerFactory.getLogger(SQLQueryLoader.class);
    private static Properties property = new Properties();

    static {
        try {
            URL url = SQLQueryLoader.class.getResource("/ru/kyrsach/kyrsach/statements.properties");
            if (url == null) {
                logger.error("Файл statements.properties не найден в ресурсах");
            }
            assert url != null;
            FileInputStream fis = new FileInputStream(url.getFile());
            property.load(fis);
            fis.close();

        } catch (IOException e) {
            logger.error("Ошибка загрузки SQL-запросов", e);
        }
    }

    public static String getQuery(String key) {
        String query = property.getProperty(key);

        if (query == null) {
            logger.warn("SQL-запрос с ключом '{}' не найден", key);
        }
        return query;
    }
}
