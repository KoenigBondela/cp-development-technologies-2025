package com.hoteldb.labs.jpa;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class JpaFactory {
    private static final String DEFAULT_PROPERTIES_FILE = "database.properties";

    private JpaFactory() {
    }

    public static EntityManagerFactory createEntityManagerFactory(String persistenceUnitName) {
        if (persistenceUnitName == null || persistenceUnitName.isBlank()) {
            throw new IllegalArgumentException("persistenceUnitName is required");
        }

        if (!"hotelPU".equals(persistenceUnitName)) {
            return Persistence.createEntityManagerFactory(persistenceUnitName);
        }

        Map<String, Object> overrides = loadJdbcOverridesFromDatabaseProperties();
        return Persistence.createEntityManagerFactory(persistenceUnitName, overrides);
    }

    private static Map<String, Object> loadJdbcOverridesFromDatabaseProperties() {
        Properties props = new Properties();
        try (InputStream in = JpaFactory.class.getClassLoader().getResourceAsStream(DEFAULT_PROPERTIES_FILE)) {
            if (in != null) {
                props.load(in);
            }
        } catch (Exception ignored) {
            // If we can't load it, fallback to persistence.xml defaults.
        }

        String driver = trimToNull(props.getProperty("db.driver"));
        String url = trimToNull(props.getProperty("db.url"));
        String username = trimToNull(props.getProperty("db.username"));
        String password = props.getProperty("db.password"); // allow empty password

        Map<String, Object> map = new HashMap<>();
        if (driver != null) map.put("jakarta.persistence.jdbc.driver", driver);
        if (url != null) map.put("jakarta.persistence.jdbc.url", url);
        if (username != null) map.put("jakarta.persistence.jdbc.user", username);
        if (password != null) map.put("jakarta.persistence.jdbc.password", password);

        return map;
    }

    private static String trimToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}

