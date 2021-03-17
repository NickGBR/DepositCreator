package ru.interns.deposit.config;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DeploymentMode;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.log4j2.Log4J2Logger;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.springframework.boot.autoconfigure.IgniteConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.interns.deposit.dto.ApacheIgniteDTO;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Properties;

import static org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi.DFLT_PORT;

@Configuration
public class ApacheIgniteConfig {
    private Ignite ignite;

/*    @Bean
    public IgniteConfiguration igniteConfiguration() throws IgniteCheckedException {
        // If you provide a whole ClientConfiguration bean then configuration properties will not be used.
        IgniteConfiguration cfg = new IgniteConfiguration();
        IgniteLogger igniteLogger = new Log4J2Logger("src/main/resources/ignite-log4j.xml");
        cfg.setGridLogger(igniteLogger);
        // The node will be started as a client node.
        cfg.setClientMode(true);

        // Classes of custom Java logic will be transferred over the wire from this app.
        cfg.setPeerClassLoadingEnabled(true);

        // Setting up an IP Finder to ensure the client can locate the servers.
        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
        cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));
        cfg.setIgniteInstanceName("my-ignite");
        // Starting the node
        ignite = Ignition.start(cfg);
        ((TcpDiscoverySpi) ignite.configuration().getDiscoverySpi())
                .getIpFinder()
                .registerAddresses(Collections.singletonList(new InetSocketAddress("localhost", DFLT_PORT)));
        return cfg;
    }

    public Ignite getIgnite() {
        return ignite;
    }*/

/*    @Bean
    public IgniteConfiguration igniteConfiguration() throws IgniteCheckedException {
        // If you provide a whole ClientConfiguration bean then configuration properties will not be used.
        IgniteConfiguration cfg = new IgniteConfiguration();
        IgniteLogger igniteLogger = new Log4J2Logger("src/main/resources/ignite-log4j.xml");
        cfg.setPeerClassLoadingEnabled(true);
        cfg.setGridLogger(igniteLogger);
        cfg.setIgniteInstanceName("properties-instance-name");
        //Ignition.start(cfg);

        return cfg;
    }*/

    @Bean
    public IgniteConfigurer configurer() {
        return cfg -> {
            //Setting consistent id.
            //See `application.yml` for the additional properties.
            IgniteLogger igniteLogger = null;
            CacheConfiguration<String, ApacheIgniteDTO> cacheConfiguration = new CacheConfiguration<>("my-cache2");
            //cacheConfiguration.setCacheMode(CacheMode.LOCAL);
            cacheConfiguration.setBackups(1);
            cacheConfiguration.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
            //cacheConfiguration

            cacheConfiguration.setIndexedTypes(
                    String.class, ApacheIgniteDTO.class
            );
            try {
                igniteLogger = new Log4J2Logger("src/main/resources/ignite-log4j.xml");
            } catch (IgniteCheckedException e) {
                e.printStackTrace();
            }
            //cfg.setPeerClassLoadingEnabled(true);
            cfg.setGridLogger(igniteLogger);
            //cfg.setIgniteInstanceName("ee");
            //cfg.setClassLoader(ClassLoader.getSystemClassLoader());

            //cfg.setConsistentId("consistent-id");
            cfg.setPeerClassLoadingEnabled(true);
            //cfg.setClientMode(true);
            //Ignition.setClientMode(true);
            cfg.setCacheConfiguration(cacheConfiguration);
            //.setDeploymentMode(Dep)
            //cfg.setDeploymentMode(DeploymentMode.CONTINUOUS);
            //cfg.setCommunicationSpi(new TcpCommunicationSpi());
            Ignition.getOrStart(cfg);
        };
    }

    @PreDestroy
    public void destroy() {
        Ignition.kill(true);
    }

/*    @Bean
    public Ignite igniteConfiguration2() throws IgniteCheckedException {
        // If you provide a whole ClientConfiguration bean then configuration properties will not be used.
        Ignite start = Ignition..start(igniteConfiguration());
        return start;
    }*/
}
