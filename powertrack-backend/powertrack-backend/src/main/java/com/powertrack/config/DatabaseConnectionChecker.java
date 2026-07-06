package com.powertrack.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DatabaseConnectionChecker implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionChecker.class);

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            String dbName = connection.getCatalog();
            String dbUrl = connection.getMetaData().getURL();
            String dbVersion = connection.getMetaData().getDatabaseProductVersion();

            logger.info("╔════════════════════════════════════════════════════════════════╗");
            logger.info("║                                                                ║");
            logger.info("║          ✅ DATABASE CONNECTED SUCCESSFULLY! ✅               ║");
            logger.info("║                                                                ║");
            logger.info("║  Database Name: {}                              ", dbName);
            logger.info("║  Database Version: {}                           ", dbVersion);
            logger.info("║  Connection URL: {}  ", dbUrl);
            logger.info("║                                                                ║");
            logger.info("╚════════════════════════════════════════════════════════════════╝");

        } catch (Exception e) {
            logger.error("╔════════════════════════════════════════════════════════════════╗");
            logger.error("║                                                                ║");
            logger.error("║          ❌ DATABASE CONNECTION FAILED! ❌                    ║");
            logger.error("║                                                                ║");
            logger.error("║  Error: {}                                      ", e.getMessage());
            logger.error("║                                                                ║");
            logger.error("╚════════════════════════════════════════════════════════════════╝");
            throw e;
        }
    }
}