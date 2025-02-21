//package gov.cdc.nnddatapollservice.configuration;
//
//import jakarta.persistence.EntityManagerFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//
//@Configuration
//@EnableJpaRepositories(
//        entityManagerFactoryRef = "nbsOdseEntityManagerFactory",
//        transactionManagerRef = "nbsOdseTransactionManager",
//        basePackages = {
//                "gov.cdc.nnddatapollservice.repository.nbs_odse",
//        }
//)
//public class EdxActivityDataSourceConfig {
//    @Value("${spring.datasource.driverClassName}")
//    private String driverClassName;
//
//    @Value("${spring.datasource.odse.url}")
//    private String dbUrl;
//
//    @Value("${spring.datasource.username}")
//    private String dbUserName;
//
//    @Value("${spring.datasource.password}")
//    private String dbUserPassword;
//
//    @Bean(name = "nbsOdseDataSource")
//    @Lazy
//    public DataSource nbsOdseDataSource() {
//        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//
//        dataSourceBuilder.driverClassName(driverClassName);
//        dataSourceBuilder.url(dbUrl);
//        dataSourceBuilder.username(dbUserName);
//        dataSourceBuilder.password(dbUserPassword);
//
//        return dataSourceBuilder.build();
//    }
//    @Bean(name = "nbsOdseJdbcTemplate")
//    public JdbcTemplate nbsOdseJdbcTemplate(@Qualifier("nbsOdseDataSource") DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
//
//
//    // JPA Configurations
//
//    @Bean(name = "nbsOdseEntityManagerFactoryBuilder")
//    public EntityManagerFactoryBuilder nbsOdseEntityManagerFactoryBuilder() {
//        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
//    }
//
//    @Bean(name = "nbsOdseEntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean nbsOdseEntityManagerFactory(
//            @Qualifier("nbsOdseEntityManagerFactoryBuilder") EntityManagerFactoryBuilder builder,
//            @Qualifier("nbsOdseDataSource") DataSource dataSource) {
//        return builder
//                .dataSource(dataSource)
//                .packages("gov.cdc.nnddatapollservice.repository.nbs_odse") // Adjust package for your entities
//                .persistenceUnit("nbsodse")
//                .build();
//    }
//
//    @Bean(name = "nbsOdseTransactionManager")
//    public PlatformTransactionManager nbsOdseTransactionManager(
//            @Qualifier("nbsOdseEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
//        return new JpaTransactionManager(entityManagerFactory);
//    }
//}