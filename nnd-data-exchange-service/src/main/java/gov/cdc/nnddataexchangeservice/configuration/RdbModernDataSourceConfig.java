package gov.cdc.nnddataexchangeservice.configuration;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
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
        entityManagerFactoryRef = "rdbModernEntityManagerFactory",
        transactionManagerRef = "rdbModernTransactionManager",
        basePackages = {
                "gov.cdc.nnddataexchangeservice.repository.rdbModern"
        }
)
public class RdbModernDataSourceConfig {
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.rdbModern.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUserName;

    @Value("${spring.datasource.password}")
    private String dbUserPassword;

    @Bean(name = "rdbModernDataSource")
    public DataSource rdbModernDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();

        dataSourceBuilder.driverClassName(driverClassName);
        dataSourceBuilder.url(dbUrl);
        dataSourceBuilder.username(dbUserName);
        dataSourceBuilder.password(dbUserPassword);

        return dataSourceBuilder.build();
    }

    @Bean(name = "rdbModernEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder rdbModernEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }

    @Primary
    @Bean(name = "rdbModernEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean rdbModernEntityManagerFactory(
            @Qualifier("rdbModernEntityManagerFactoryBuilder") EntityManagerFactoryBuilder builder,
            @Qualifier("rdbModernDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("gov.cdc.nnddataexchangeservice.repository.rdbModern.model")
                .persistenceUnit("rdbModern")
                .build();
    }

    @Primary
    @Bean(name = "rdbModernTransactionManager")
    public PlatformTransactionManager rdbModernTransactionManager(
            @Qualifier("rdbModernEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }


    @Bean(name = "rdbModernJdbcTemplate")
    public JdbcTemplate rdbModernJdbcTemplate(@Qualifier("rdbModernDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
