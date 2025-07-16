package gov.cdc.nnddataexchangeservice.configuration;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class CrossDbDataSourceConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.rdb.url}") // One connection string to the instance (any default DB)
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUserName;

    @Value("${spring.datasource.password}")
    private String dbUserPassword;

    @Value("${spring.datasource.hikari.maximum-pool-size:50}")
    private int maximumPoolSize;

    @Value("${spring.datasource.hikari.minimum-idle:10}")
    private int minimumIdle;

    @Value("${spring.datasource.hikari.idle-timeout:600000}")
    private long idleTimeout;

    @Value("${spring.datasource.hikari.max-lifetime:1800000}")
    private long maxLifetime;

    @Value("${spring.datasource.hikari.connection-timeout:30000}")
    private long connectionTimeout;

    @Value("${spring.datasource.hikari.pool-name.crossdb:CrossDbHikariCP}")
    private String poolName;

    @Value("${spring.datasource.hikari.keepalive-time:300000}")
    private long keepaliveTime;

    @Value("${spring.datasource.hikari.validation-timeout:500}")
    private long validationTimeout;

    @Bean(name = "crossDbDataSource")
    public DataSource crossDbDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUserName);
        config.setPassword(dbUserPassword);
        config.setMaximumPoolSize(maximumPoolSize);
        config.setMinimumIdle(minimumIdle);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifetime);
        config.setConnectionTimeout(connectionTimeout);
        config.setPoolName(poolName);
        config.setKeepaliveTime(keepaliveTime);
        config.setValidationTimeout(validationTimeout);
        config.setConnectionTestQuery("SELECT 1");

        return new HikariDataSource(config);
    }

    @Bean(name = "crossDbJdbcTemplate")
    public JdbcTemplate crossDbJdbcTemplate(DataSource crossDbDataSource) {
        return new JdbcTemplate(crossDbDataSource);
    }
}