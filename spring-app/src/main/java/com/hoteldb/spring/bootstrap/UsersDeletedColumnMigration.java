package com.hoteldb.spring.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Добавляет колонку users.deleted в существующую БД (Лаб8), если её ещё нет.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UsersDeletedColumnMigration implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(UsersDeletedColumnMigration.class);

    private final JdbcTemplate jdbcTemplate;

    public UsersDeletedColumnMigration(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        Integer exists = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM information_schema.COLUMNS
                WHERE TABLE_SCHEMA = DATABASE()
                  AND TABLE_NAME = 'users'
                  AND COLUMN_NAME = 'deleted'
                """,
                Integer.class);
        if (exists != null && exists > 0) {
            return;
        }
        jdbcTemplate.execute(
                "ALTER TABLE users ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT FALSE");
        log.info("Added column users.deleted (soft-delete for Lab8)");
    }
}
