package br.com.unipix.api.config;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "smstionEntityManagerFactory",
					   transactionManagerRef = "smstionTransactionManager",
					   basePackages = { "br.com.unipix.api.repository" })
public class SmsMsqlConfig {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsMsqlConfig.class);

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Primary
	@Bean(name = "smsDataSource")
    public HikariDataSource  smstionDataSource() {
        LOGGER.info("Creating Primary Datasource Mysql");

		HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        
        dataSource.setMaximumPoolSize(20);
        dataSource.setPoolName("SmsConsumoSumarizadoService");
        dataSource.setMaxLifetime(2000000);
        dataSource.setConnectionTimeout(30000);

        return dataSource;
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean smstionEntityManagerFactory() {
        LOGGER.info("Creating Primary Entity Manager Factory");

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(smstionDataSource());
        em.setPackagesToScan("br.com.unipix.api.model");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL57Dialect");
        em.setJpaPropertyMap(properties);
 
        return em;
    }

    @Primary
    @Bean
    public PlatformTransactionManager smstionTransactionManager() {
        LOGGER.info("Creating Primary Transaction Manager");

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(smstionEntityManagerFactory().getObject());
        return transactionManager;
    }

}