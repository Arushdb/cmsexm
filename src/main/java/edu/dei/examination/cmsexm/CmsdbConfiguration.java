package edu.dei.examination.cmsexm;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
@EnableTransactionManagement
public class CmsdbConfiguration {

    @Bean(name = "cmsDataSource")
    @ConfigurationProperties(prefix = "spring.third-datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "cmsJdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("cmsDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // You can also define a TransactionManager if you need transaction support for JdbcTemplate
    @Bean(name = "cmsTransactionManager")
    public PlatformTransactionManager cmsTransactionManager(@Qualifier("cmsDataSource") DataSource dataSource) {
        return new org.springframework.jdbc.datasource.DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "cmsTransactionTemplate")
    public TransactionTemplate transactionTemplate(@Qualifier("cmsTransactionManager") PlatformTransactionManager cmsTransactionManager) {
        return new TransactionTemplate(cmsTransactionManager);
    }
}
