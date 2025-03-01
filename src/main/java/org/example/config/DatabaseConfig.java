package org.example.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


/**
 * TODO: Основные функции класса:
 * -----------------------------------------------------------------------
 *     Настройка подключения к базе данных:
 *         Создает источник данных (DataSource) с параметрами подключения
 *         Управляет учетными данными для доступа к базе данных
 *         Определяет стратегию создания таблиц (ddl-auto)
 *     Настройка JPA:
 *         Создает адаптер для работы с JPA (JpaVendorAdapter)
 *         Настраивает платформу базы данных (MySQL)
 *         Управляет созданием и обновлением схемы базы данных
 *     Управление транзакциями:
 *         Создает менеджер транзакций (PlatformTransactionManager)
 *         Обеспечивает атомарность операций с базой данных
 *         Управляет началом, подтверждением и откатом транзакций
 *     Обработка исключений:
 *         Создает процессор для перевода исключений базы данных
 *         Преобразует низкоуровневые исключения в понятные приложению
 *         Обеспечивает единообразную обработку ошибок базы данных ,
 * ---------------------------------------------------------------------------
 */



@Configuration
@EnableJpaRepositories(basePackages = "org.example.repository")
public class DatabaseConfig {

    @Value("${spring.datasource.url}") // URL подключения к базе данны
    private String databaseUrl;

    @Value("${spring.datasource.username}") // имя пользователя базы данных
    private String databaseUsername;

    @Value("${spring.datasource.password}") // пароль пользователя базы данных
    private String databasePassword;

    private DataSource dataSource() { // создает источник данных для подключения к базе
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url(databaseUrl)
                .username(databaseUsername)
                .password(databasePassword)
                .build();
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() { // настраивает адаптер для JPA
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
        return vendorAdapter;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) { // управляет транзакциями
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() { // переводит исключения базы данных
        return new PersistenceExceptionTranslationPostProcessor();
    }
}

