package gov.cdc.nnddataexchangeservice.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gov.cdc.nnddataexchangeservice.property.DbPropertiesProvider;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "rdbEntityManagerFactory",
        transactionManagerRef = "rdbTransactionManager",
        basePackages = {
                "gov.cdc.nnddataexchangeservice.repository.rdb"
        }
)
public class RdbDataSourceConfig {
    private final DbPropertiesProvider dbPropertiesProvider;



    @Value("${spring.datasource.rdb.url}")
    private String dbUrl;

    @Value("${spring.datasource.hikari.pool-name.rdb:OdseHikariCP}")
    private String poolName;

    public RdbDataSourceConfig(DbPropertiesProvider dbPropertiesProvider) {
        this.dbPropertiesProvider = dbPropertiesProvider;
    }

    @Bean(name = "rdbDataSource")
    public DataSource rdbDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(dbPropertiesProvider.getDriverClassName());
        hikariConfig.setJdbcUrl(dbUrl);
        hikariConfig.setUsername(dbPropertiesProvider.getDbUserName());
        hikariConfig.setPassword(dbPropertiesProvider.getDbUserPassword());

        // HikariCP-specific settings
        hikariConfig.setMaximumPoolSize(dbPropertiesProvider.getMaximumPoolSize());
        hikariConfig.setMinimumIdle(dbPropertiesProvider.getMinimumIdle());
        hikariConfig.setIdleTimeout(dbPropertiesProvider.getIdleTimeout());
        hikariConfig.setMaxLifetime(dbPropertiesProvider.getMaxLifetime());
        hikariConfig.setConnectionTimeout(dbPropertiesProvider.getConnectionTimeout());
        hikariConfig.setPoolName(poolName);

        hikariConfig.setKeepaliveTime(dbPropertiesProvider.getKeepaliveTime());
        hikariConfig.setValidationTimeout(dbPropertiesProvider.getValidationTimeout());
        hikariConfig.setInitializationFailTimeout(-1);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        return new HikariDataSource(hikariConfig);
    }

    @Bean(name = "rdbEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder rdbEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }

    @Primary
    @Bean(name = "rdbEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean rdbEntityManagerFactory(
            @Qualifier("rdbEntityManagerFactoryBuilder") EntityManagerFactoryBuilder builder,
            @Qualifier("rdbDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("gov.cdc.nnddataexchangeservice.repository.rdb.model")
                .persistenceUnit("rdb")
                .build();
    }

    @Primary
    @Bean(name = "rdbTransactionManager")
    public PlatformTransactionManager rdbTransactionManager(
            @Qualifier("rdbEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }


    @Bean(name = "rdbJdbcTemplate")
    public JdbcTemplate rdbJdbcTemplate(@Qualifier("rdbDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
