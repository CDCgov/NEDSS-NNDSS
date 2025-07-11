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
        entityManagerFactoryRef = "srteEntityManagerFactory",
        transactionManagerRef = "srteTransactionManager",
        basePackages = {
                "gov.cdc.nnddataexchangeservice.repository.srte"
        }
)
public class SrteDataSourceConfig {
    private final DbPropertiesProvider dbPropertiesProvider;

    @Value("${spring.datasource.srte.url}")
    private String dbUrl;

    @Value("${spring.datasource.hikari.pool-name.srte:OdseHikariCP}")
    private String poolName;

    public SrteDataSourceConfig(DbPropertiesProvider dbPropertiesProvider) {
        this.dbPropertiesProvider = dbPropertiesProvider;
    }

    @Bean(name = "srteDataSource")
    public DataSource srteDataSource() {
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

    @Bean(name = "srteEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder srteEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }

    @Primary
    @Bean(name = "srteEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean srteEntityManagerFactory(
            @Qualifier("srteEntityManagerFactoryBuilder") EntityManagerFactoryBuilder builder,
            @Qualifier("srteDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("gov.cdc.nnddataexchangeservice.repository.srte.model")
                .persistenceUnit("srte")
                .build();
    }

    @Primary
    @Bean(name = "srteTransactionManager")
    public PlatformTransactionManager srteTransactionManager(
            @Qualifier("srteEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }


    @Bean(name = "srteJdbcTemplate")
    public JdbcTemplate srteJdbcTemplate(@Qualifier("srteDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
