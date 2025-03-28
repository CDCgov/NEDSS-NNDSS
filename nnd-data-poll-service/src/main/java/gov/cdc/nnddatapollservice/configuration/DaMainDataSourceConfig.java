package gov.cdc.nnddatapollservice.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "rdbEntityManagerFactory",
        transactionManagerRef = "rdbTransactionManager",
        basePackages = {
                "gov.cdc.nnddatapollservice.repository.rdb_modern",
                "gov.cdc.nnddatapollservice.repository.srte",
                "gov.cdc.nnddatapollservice.repository.config",
                "gov.cdc.nnddatapollservice.repository.nbs_odse",
        }
)
@EnableTransactionManagement
public class DaMainDataSourceConfig {
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.dataSync.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUserName;

    @Value("${spring.datasource.password}")
    private String dbUserPassword;


    @Value("${spring.datasource.hikari.maximum-pool-size:100}")
    private int maximumPoolSize;

    @Value("${spring.datasource.hikari.minimum-idle:50}")
    private int minimumIdle;

    @Value("${spring.datasource.hikari.idle-timeout:120000}")
    private long idleTimeout;

    @Value("${spring.datasource.hikari.max-lifetime:1200000}")
    private long maxLifetime;

    @Value("${spring.datasource.hikari.connection-timeout:300000}")
    private long connectionTimeout;

    @Value("${spring.datasource.hikari.pool-name:OdseHikariCP}")
    private String poolName;


    @Bean(name = "rdbDataSource")
    @Lazy
    public DataSource rdbDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setJdbcUrl(dbUrl);
        hikariConfig.setUsername(dbUserName);
        hikariConfig.setPassword(dbUserPassword);

        // HikariCP-specific settings
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        hikariConfig.setMinimumIdle(minimumIdle);
        hikariConfig.setIdleTimeout(idleTimeout);
        hikariConfig.setMaxLifetime(maxLifetime);
        hikariConfig.setConnectionTimeout(connectionTimeout);
        hikariConfig.setPoolName(poolName);

        return new HikariDataSource(hikariConfig);
    }
    @Bean(name = "rdbJdbcTemplate")
    public JdbcTemplate rdbJdbcTemplate(@Qualifier("rdbDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


    // JPA Configurations

    @Bean(name = "rdbEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder rdbEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }

    @Bean(name = "rdbEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean rdbEntityManagerFactory(
            @Qualifier("rdbEntityManagerFactoryBuilder") EntityManagerFactoryBuilder builder,
            @Qualifier("rdbDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("gov.cdc.nnddatapollservice.repository.rdb_modern",
                        "gov.cdc.nnddatapollservice.repository.srte",
                        "gov.cdc.nnddatapollservice.repository.config",
                        "gov.cdc.nnddatapollservice.repository.nbs_odse") // Adjust package for your entities
                .persistenceUnit("dataSync")
                .build();
    }

    @Primary
    @Bean(name = "rdbTransactionManager")
    public PlatformTransactionManager rdbTransactionManager(
            @Qualifier("rdbEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}