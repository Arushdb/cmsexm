package edu.dei.examination.cmsexm;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "examEntityManagerFactory",
    transactionManagerRef = "examTransactionManager", basePackages = {"edu.dei.examination.cmsexm.repository"})
public class ExamdbConfiguration {
	
	@Primary
	@Bean(name = "examDataSource")
	  @ConfigurationProperties(prefix = "spring.datasource")
	  public DataSource dataSource() {
	    return DataSourceBuilder.create().build();
	  }
	  @Primary
	  @Bean(name = "examEntityManagerFactory")
	  public LocalContainerEntityManagerFactoryBean barEntityManagerFactory(
	      EntityManagerFactoryBuilder builder, @Qualifier("examDataSource") DataSource dataSource) {
	    return builder.dataSource(dataSource).packages("edu.dei.examination.cmsexm.model").persistenceUnit("exam")
	        .build();
	  }
	@Primary
	  @Bean(name = "examTransactionManager")
	  public PlatformTransactionManager barTransactionManager(
	      @Qualifier("examEntityManagerFactory") EntityManagerFactory examEntityManagerFactory) {
	    return new JpaTransactionManager(examEntityManagerFactory);
	  }


}
