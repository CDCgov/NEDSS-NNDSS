//package gov.cdc.nnddataexchangeservice.configuration;
//
//import org.apache.catalina.connector.Connector;
//import org.apache.coyote.AbstractProtocol;
//import org.apache.coyote.ProtocolHandler;
//import org.apache.coyote.http11.Http11NioProtocol;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class TomcatConfig {
//
//    @Value("${server.tomcat.max-connections}")
//    private int maxConnections;
//
//    @Value("${server.tomcat.connection-timeout}")
//    private int connectionTimeout;
//
//    @Value("${server.tomcat.accept-count}")
//    private int acceptCount;
//
//    @Value("${server.tomcat.threads.max}")
//    private int maxThreads;
//
//    @Value("${server.tomcat.threads.min-spare}")
//    private int minSpareThreads;
//
//    @Value("${server.tomcat.keep-alive-timeout}")
//    private int keepAliveTimeout;
//
//    @Value("${server.tomcat.max-keep-alive-requests}")
//    private int maxKeepAliveRequests;
//
//    @Bean
//    public TomcatServletWebServerFactory tomcatFactory() {
//        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
//
//        factory.addConnectorCustomizers((Connector connector) -> {
//            ProtocolHandler handler = connector.getProtocolHandler();
//            if (handler instanceof Http11NioProtocol protocol) {
//                protocol.setMaxConnections(maxConnections);
//                protocol.setConnectionTimeout(connectionTimeout);
//                protocol.setAcceptCount(acceptCount);
//                protocol.setMaxThreads(maxThreads);
//                protocol.setMinSpareThreads(minSpareThreads);
//                protocol.setKeepAliveTimeout(keepAliveTimeout);
//                protocol.setMaxKeepAliveRequests(maxKeepAliveRequests);
//            }
//        });
//
//        return factory;
//    }
//}