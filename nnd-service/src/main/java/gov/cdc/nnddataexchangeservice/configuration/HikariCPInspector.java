//package gov.cdc.nnddataexchangeservice.configuration;
//
//import com.zaxxer.hikari.HikariDataSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class HikariCPInspector {
//
//    private HikariDataSource dataSource;
//
//    public HikariCPInspector(@Qualifier("rdbDataSource") HikariDataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//
//
//    @Bean
//    public CommandLineRunner logHikariConfig() {
//        return args -> {
//            System.out.println("HikariCP Pool Name: " + dataSource.getPoolName());
//            System.out.println("Max Pool Size: " + dataSource.getMaximumPoolSize());
//            System.out.println("Min Idle Connections: " + dataSource.getMinimumIdle());
//            System.out.println("Idle Timeout: " + dataSource.getIdleTimeout());
//            System.out.println("Max Lifetime: " + dataSource.getMaxLifetime());
//            System.out.println("Validation Timeout: " + dataSource.getValidationTimeout());
//        };
//    }
//}