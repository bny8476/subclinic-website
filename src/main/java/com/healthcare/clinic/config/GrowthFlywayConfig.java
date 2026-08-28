package com.healthcare.clinic.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class GrowthFlywayConfig {

    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(GrowthFlywayConfig.class);

    @Bean
    public Flyway growthFlyway(
            @Qualifier("growthDataSource") DataSource growthDataSource,
            Environment env) {

        boolean baselineOnMigrate =
                env.getProperty(
                        "app.flyway.baseline-on-migrate",
                        Boolean.class,
                        true
                );

        String baselineVersion =
                env.getProperty(
                        "app.flyway.baseline-version-growth",
                        "0"
                );

        Flyway flyway = Flyway.configure()
                .dataSource(growthDataSource)
                .locations("classpath:db/migration/growth")
                .table("growth_flyway_schema_history")
                .baselineOnMigrate(baselineOnMigrate)
                .baselineVersion(baselineVersion)
                .load();

        if (Boolean.parseBoolean(
                env.getProperty("spring.flyway.enabled", "true"))) {

            migrateWithRetry(flyway, "growth");
        }

        return flyway;
    }

    private void migrateWithRetry(Flyway flyway, String dbName) {
        try {
            flyway.baseline();
            flyway.migrate();
        } catch (Exception ex) {
            logger.warn("Flyway {} migration exception: {}", dbName, ex.getMessage());
        }
    }
}
