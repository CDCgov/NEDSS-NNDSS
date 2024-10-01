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
        entityManagerFactoryRef = "rdbmodernEntityManagerFactory",
        transactionManagerRef = "rdbmodernTransactionManager",
        basePackages = {
                "gov.cdc.nnddatapollservice.repository.rdb_modern",
        }
)
public class RdbModernDataSourceConfig {

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.rdb_modern.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUserName;

    @Value("${spring.datasource.password}")
    private String dbUserPassword;

    // DataSource Bean
    @Bean(name = "rdbmodernDataSource")
    public DataSource rdbmodernDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(dbUrl)
                .username(dbUserName)
                .password(dbUserPassword)
                .build();
    }

    // JdbcTemplate Bean
    @Bean(name = "rdbmodernJdbcTemplate")
    public JdbcTemplate rdbmodernJdbcTemplate(@Qualifier("rdbmodernDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // JPA Configurations

    @Bean(name = "rdbModernEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder rdbModernEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }

    @Bean(name = "rdbmodernEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean rdbModernEntityManagerFactory(
            @Qualifier("rdbModernEntityManagerFactoryBuilder") EntityManagerFactoryBuilder builder,
            @Qualifier("rdbmodernDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("gov.cdc.nnddatapollservice.repository.rdb_modern") // Adjust package for your entities
                .persistenceUnit("rdb_modern")
                .build();
    }

    @Bean(name = "rdbmodernTransactionManager")
    public PlatformTransactionManager rdbmodernTransactionManager(
            @Qualifier("rdbmodernEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
