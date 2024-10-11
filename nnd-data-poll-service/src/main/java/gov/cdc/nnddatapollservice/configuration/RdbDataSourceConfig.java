package gov.cdc.nnddatapollservice.configuration;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "rdbEntityManagerFactory",
        transactionManagerRef = "rdbTransactionManager",
        basePackages = {
                "gov.cdc.nnddatapollservice.repository.rdb_modern",
        }
)
public class RdbDataSourceConfig {
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.rdb.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUserName;

    @Value("${spring.datasource.password}")
    private String dbUserPassword;

    @Bean(name = "rdbDataSource")
    public DataSource rdbDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();

        dataSourceBuilder.driverClassName(driverClassName);
        dataSourceBuilder.url(dbUrl);
        dataSourceBuilder.username(dbUserName);
        dataSourceBuilder.password(dbUserPassword);

        return dataSourceBuilder.build();
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
                .packages("gov.cdc.nnddatapollservice.repository.rdb_modern") // Adjust package for your entities
                .persistenceUnit("rdb")
                .build();
    }

    @Bean(name = "rdbTransactionManager")
    public PlatformTransactionManager rdbTransactionManager(
            @Qualifier("rdbEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}