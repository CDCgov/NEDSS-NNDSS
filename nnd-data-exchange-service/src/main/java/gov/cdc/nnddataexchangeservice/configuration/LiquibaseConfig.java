package gov.cdc.nnddataexchangeservice.configuration;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class LiquibaseConfig {

    @Value("${spring.liquibase.user}")
    private String dbUserName;

    @Value("${spring.liquibase.password}")
    private String dbUserPassword;

    @Value("${spring.liquibase.driver-class-name}")
    private String driverClassName;

    @Value("${spring.liquibase.url}")
    private String dbUrl;
    @Bean
    @ConfigurationProperties(prefix = "spring.liquibase")
    public LiquibaseProperties liquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean(name = "liquibaseDataSource")
    public DataSource liquibaseDataSource() {
        return DataSourceBuilder.create()
                .url(dbUrl)
                .username(dbUserName)
                .password(dbUserPassword)
                .driverClassName(driverClassName)
                .build();
    }

    @Bean
    public SpringLiquibase liquibase(@Qualifier("liquibaseDataSource") DataSource liquibaseDataSource, LiquibaseProperties liquibaseProperties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(liquibaseDataSource);
        liquibase.setChangeLog(liquibaseProperties.getChangeLog());
        List<String> contextsList = liquibaseProperties.getContexts();
        if (contextsList != null && !contextsList.isEmpty()) {
            liquibase.setContexts(String.join(",", contextsList));
        }

        return liquibase;
    }
}