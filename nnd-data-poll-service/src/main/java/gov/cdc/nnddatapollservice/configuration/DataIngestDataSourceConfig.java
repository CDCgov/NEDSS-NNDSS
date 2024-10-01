package gov.cdc.nnddatapollservice.configuration;


import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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
        entityManagerFactoryRef = "ingestEntityManagerFactory",
        transactionManagerRef = "ingestTransactionManager",
        basePackages = {
                "gov.cdc.nnddatapollservice.repository.msg",
                "gov.cdc.nnddatapollservice.repository.odse"
        }
)
public class DataIngestDataSourceConfig {
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.ingest.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUserName;

    @Value("${spring.datasource.password}")
    private String dbUserPassword;

    @Bean(name = "ingestDataSource")
    public DataSource ingestDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();

        dataSourceBuilder.driverClassName(driverClassName);
        dataSourceBuilder.url(dbUrl);
        dataSourceBuilder.username(dbUserName);
        dataSourceBuilder.password(dbUserPassword);

        return dataSourceBuilder.build();
    }

    @Bean(name = "ingestEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder ingestEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }

    @Bean(name = "ingestEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean ingestEntityManagerFactory(
            EntityManagerFactoryBuilder ingestEntityManagerFactoryBuilder,
            @Qualifier("ingestDataSource") DataSource ingestDataSource ) {
        return ingestEntityManagerFactoryBuilder
                .dataSource(ingestDataSource)
                .packages("gov.cdc.nnddatapollservice.repository.msg", "gov.cdc.nnddatapollservice.repository.msg",
                        "gov.cdc.nnddatapollservice.repository.msg", "gov.cdc.nnddatapollservice.repository.odse")
                .persistenceUnit("ingest")
                .build();
    }

    @Bean(name = "ingestTransactionManager")
    public PlatformTransactionManager ingestTransactionManager(
            @Qualifier("ingestEntityManagerFactory") EntityManagerFactory ingestEntityManagerFactory ) {
        return new JpaTransactionManager(ingestEntityManagerFactory);
    }
}
