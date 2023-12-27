package edu.dei.examination.cmsexm;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "phdEntityManagerFactory",
    transactionManagerRef = "phdTransactionManager", basePackages = {"edu.dei.examination.phd.repository"})
public class PhddbConfiguration {
	
	
	@Bean(name = "phdDataSource")
	  @ConfigurationProperties(prefix = "spring.second-datasource")
	  public DataSource dataSource() {
	    return DataSourceBuilder.create().build();
	  }

	  @Bean(name = "phdEntityManagerFactory")
	  public LocalContainerEntityManagerFactoryBean barEntityManagerFactory(
	      EntityManagerFactoryBuilder builder, @Qualifier("phdDataSource") DataSource dataSource) {
	    return builder.dataSource(dataSource).packages("edu.dei.examination.phd.model").persistenceUnit("phd")
	        .build();
	  }

	  @Bean(name = "phdTransactionManager")
	  public PlatformTransactionManager barTransactionManager(
	      @Qualifier("phdEntityManagerFactory") EntityManagerFactory phdEntityManagerFactory) {
	    return new JpaTransactionManager(phdEntityManagerFactory);
	  }


}
