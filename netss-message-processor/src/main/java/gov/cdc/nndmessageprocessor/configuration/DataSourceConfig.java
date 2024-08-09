package gov.cdc.nndmessageprocessor.configuration;

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
        entityManagerFactoryRef = "nndEntityManagerFactory",
        transactionManagerRef = "nndTransactionManager",
        basePackages = "gov.cdc.nndmessageprocessor.repository"
)
public class DataSourceConfig {

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.nnd.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUserName;

    @Value("${spring.datasource.password}")
    private String dbUserPassword;

    @Bean(name = "nndDataSource")
    public DataSource nndDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(dbUrl)
                .username(dbUserName)
                .password(dbUserPassword)
                .build();
    }

    @Bean(name = "nndEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder nndEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }

    @Bean(name = "nndEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean nndEntityManagerFactory(
            @Qualifier("nndEntityManagerFactoryBuilder") EntityManagerFactoryBuilder nndEntityManagerFactoryBuilder,
            @Qualifier("nndDataSource") DataSource nndDataSource) {
        return nndEntityManagerFactoryBuilder
                .dataSource(nndDataSource)
                .packages("gov.cdc.nndmessageprocessor.repository")
                .persistenceUnit("nnd")
                .build();
    }

    @Bean(name = "nndTransactionManager")
    public PlatformTransactionManager nndTransactionManager(
            @Qualifier("nndEntityManagerFactory") EntityManagerFactory nndEntityManagerFactory) {
        return new JpaTransactionManager(nndEntityManagerFactory);
    }
}