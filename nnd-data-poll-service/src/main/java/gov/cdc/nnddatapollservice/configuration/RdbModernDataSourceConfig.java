package gov.cdc.nnddatapollservice.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class RdbModernDataSourceConfig {
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.rdb_modern.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUserName;

    @Value("${spring.datasource.password}")
    private String dbUserPassword;

    @Bean(name = "rdbmodernDataSource")
    public DataSource rdbmodernDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();

        dataSourceBuilder.driverClassName(driverClassName);
        dataSourceBuilder.url(dbUrl);
        dataSourceBuilder.username(dbUserName);
        dataSourceBuilder.password(dbUserPassword);

        return dataSourceBuilder.build();
    }
    @Bean(name = "rdbmodernJdbcTemplate")
    public JdbcTemplate rdbmodernJdbcTemplate(@Qualifier("rdbmodernDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}