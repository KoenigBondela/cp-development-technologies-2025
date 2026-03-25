package com.hoteldb.labs.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Демонстрация резервной копии (primary -> backup) в другую СУБД.
 */
public class Lab1BackupMain {
    private static final Logger logger = LoggerFactory.getLogger(Lab1BackupMain.class);

    public static void main(String[] args) {
        logger.info("=== Лабораторная работа: резервная копия БД в другую СУБД ===");

        try {
            DatabaseConnection db = DatabaseConnection.getInstance();
            DatabaseBackupService backupService = new DatabaseBackupService(db);

            if (!db.isBackupConfigured()) {
                logger.error("Резервная БД не настроена. Проверь database.properties (db.backup.*)");
                System.err.println("Резервная БД не настроена. Проверь database.properties (db.backup.*)");
                return;
            }

            DatabaseBackupService.BackupReport report = backupService.backupPrimaryToBackup();
            System.out.println("Backup completed: rooms=" + report.roomsCopied() + ", clients=" + report.clientsCopied());
        } catch (Exception e) {
            logger.error("Ошибка резервного копирования: {}", e.getMessage(), e);
            System.err.println("Ошибка резервного копирования: " + e.getMessage());
        }
    }
}

