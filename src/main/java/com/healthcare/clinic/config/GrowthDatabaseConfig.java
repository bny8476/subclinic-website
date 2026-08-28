package com.healthcare.clinic.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.context.annotation.DependsOn;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.healthcare.clinic",
        entityManagerFactoryRef = "growthEntityManagerFactory",
        transactionManagerRef = "growthTransactionManager",
        nameGenerator = org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator.class
)
public class GrowthDatabaseConfig {

    @Autowired
    private org.springframework.core.env.Environment environment;

    @Primary
    @Bean(name = "growthDataSource")
    @ConfigurationProperties(prefix = "app.datasource.growth")
    public DataSource dataSource() {
        String url = environment.getProperty("app.datasource.growth.url");
        if (url == null || url.trim().isEmpty()) {
            url = environment.getProperty("SPRING_DATASOURCE_GROWTH_URL");
        }
        
        String username = environment.getProperty("app.datasource.growth.username");
        if (username == null || username.trim().isEmpty()) {
            username = environment.getProperty("SPRING_DATASOURCE_GROWTH_USERNAME");
        }
        
        String password = environment.getProperty("app.datasource.growth.password");
        if (password == null || password.trim().isEmpty()) {
            password = environment.getProperty("SPRING_DATASOURCE_GROWTH_PASSWORD");
        }
        
        String driver = environment.getProperty("app.datasource.growth.driver-class-name");
        if (driver == null || driver.trim().isEmpty()) {
            driver = environment.getProperty("SPRING_DATASOURCE_GROWTH_DRIVER_CLASS_NAME");
        }

        boolean isRender = java.util.Arrays.asList(environment.getActiveProfiles()).contains("render");
        boolean isH2Fallback = url == null || url.trim().isEmpty() || url.contains("jdbc:h2");
        
        if (isRender) {
            if (isH2Fallback) throw new IllegalStateException("FATAL: SPRING_DATASOURCE_GROWTH_URL is missing in production.");
            if (username == null || username.trim().isEmpty()) throw new IllegalStateException("FATAL: Growth Database username is missing in production.");
        }

        if (isH2Fallback) {
            url = "jdbc:h2:mem:growthdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;NON_KEYWORDS=VALUE";
            driver = "org.h2.Driver";
        } else {
            driver = (driver != null && !driver.trim().isEmpty()) ? driver : (url.startsWith("jdbc:postgresql") ? "org.postgresql.Driver" : "org.postgresql.Driver");
        }

        username = (username != null && !username.trim().isEmpty()) ? username : "sa";
        password = (password != null) ? password : "";

        com.zaxxer.hikari.HikariDataSource dataSource = new com.zaxxer.hikari.HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driver);
        dataSource.setKeepaliveTime(environment.getProperty("app.datasource.growth.keepalive-time", Long.class, 120000L));
        dataSource.setConnectionTestQuery("SELECT 1");
        dataSource.setMaximumPoolSize(environment.getProperty("app.datasource.growth.maximum-pool-size", Integer.class, 5));
        dataSource.setMinimumIdle(environment.getProperty("app.datasource.growth.minimum-idle", Integer.class, 1));

        logInfo(url);
        return dataSource;
    }

    private void logInfo(String url) {
        System.out.println("Configured Growth DataSource URL: " + url);
    }

    @Primary
    @Bean(name = "growthEntityManagerFactory")
    @DependsOn({"growthFlyway"})
    public LocalContainerEntityManagerFactoryBean growthEntityManagerFactory(
            @Qualifier("growthDataSource") DataSource dataSource,
            org.springframework.core.env.Environment env) {
        
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPersistenceUnitName("growth");
        em.setPackagesToScan("com.healthcare.clinic");

        em.setJpaVendorAdapter(new org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter());
        
        java.util.HashMap<String, Object> properties = new java.util.HashMap<>();
        String driver = env.getProperty("app.datasource.growth.driver-class-name", "org.postgresql.Driver");
        String dialect = "org.hibernate.dialect.PostgreSQLDialect";
        if (driver.contains("mysql")) dialect = "org.hibernate.dialect.MySQLDialect";
        else if (driver.contains("h2")) dialect = "org.hibernate.dialect.H2Dialect";
        
        String ddlAuto = env.getProperty("spring.jpa.hibernate.ddl-auto", "validate");
        properties.put("hibernate.dialect", dialect);
        properties.put("hibernate.hbm2ddl.auto", ddlAuto);
        properties.put("hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
        em.setJpaPropertyMap(properties);
        
        return em;
    }

    @Primary
    @Bean(name = "growthTransactionManager")
    public PlatformTransactionManager growthTransactionManager(
            @Qualifier("growthEntityManagerFactory") EntityManagerFactory growthEntityManagerFactory) {
        return new JpaTransactionManager(growthEntityManagerFactory);
    }
}
