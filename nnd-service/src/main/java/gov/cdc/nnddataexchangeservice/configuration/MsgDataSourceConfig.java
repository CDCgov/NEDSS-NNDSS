package gov.cdc.nnddataexchangeservice.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        entityManagerFactoryRef = "msgEntityManagerFactory",
        transactionManagerRef = "msgTransactionManager",
        basePackages = {
                "gov.cdc.nnddataexchangeservice.repository.msg"
        }
)
public class MsgDataSourceConfig {
    private final DbPropertiesProvider dbPropertiesProvider;

    @Value("${spring.datasource.msg.url}")
    private String dbUrl;

    @Value("${spring.datasource.hikari.pool-name.msg:OdseHikariCP}")
    private String poolName;

    public MsgDataSourceConfig(DbPropertiesProvider dbPropertiesProvider) {
        this.dbPropertiesProvider = dbPropertiesProvider;
    }

    @Bean(name = "msgDataSource")
    public DataSource msgDataSource() {
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

    @Bean(name = "msgEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder msgEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }

    @Bean(name = "msgEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean msgEntityManagerFactory(
            @Qualifier("msgEntityManagerFactoryBuilder") EntityManagerFactoryBuilder builder,
            @Qualifier("msgDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("gov.cdc.nnddataexchangeservice.repository.msg.model")
                .persistenceUnit("msg")
                .build();
    }

    @Bean(name = "msgTransactionManager")
    public PlatformTransactionManager msgTransactionManager(
            @Qualifier("msgEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean(name = "msgJdbcTemplate")
    public JdbcTemplate msgJdbcTemplate(@Qualifier("msgDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
