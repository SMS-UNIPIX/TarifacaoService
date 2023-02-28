package br.com.unipix.api.config;

import java.beans.PropertyVetoException;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "extremeEntityManagerFactory", 
					   transactionManagerRef = "extremeTransactionManager", 
					   basePackages = { "br.com.unipix.api.extream.repository" })
public class ExtremeDbConfig {

	@Autowired
	Environment env;
	
	@Bean(name = "extremeDataSource")
	public HikariDataSource dataSource() throws PropertyVetoException  {
		HikariDataSource dataSource = new HikariDataSource();
        
        String jdbcUrl = env.getProperty("extreme.datasource.url");
        String userName = env.getProperty("extreme.datasource.username");
        String passWord = env.getProperty("extreme.datasource.password");
        String driveClass = env.getProperty("extreme.datasource.driver-class-name");

        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(userName);
        dataSource.setPassword(passWord);
        dataSource.setDriverClassName(driveClass);
        
        dataSource.setMaximumPoolSize(20);
        dataSource.setIdleTimeout(60000);
        dataSource.setPoolName("Faturamento");
        dataSource.setMaxLifetime(2000000);
        dataSource.setConnectionTimeout(60000);
        dataSource.setConnectionTestQuery("SELECT 1");
        return dataSource;
	}

    @Bean(name="extremeJdbcTemplate")
	public JdbcTemplate jdbcTemplate(@Qualifier("extremeDataSource") DataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate;
	}

	@Bean(name = "extremeEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("extremeDataSource") DataSource dataSource) {
		return builder.dataSource(dataSource)
				.packages("br.com.unipix.api.extream.model").persistenceUnit("ConsumoDiarioSMS")
				.build();
	}

	@Bean(name = "extremeTransactionManager")
	public PlatformTransactionManager transactionManager(
			@Qualifier("extremeEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
}