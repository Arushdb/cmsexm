package edu.dei.examination.phd.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(2)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // simple in-memory users for demo. Replace with JDBC or LDAP in production.
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//            .withUser("supervisor1").password("{noop}sup1pass").roles("SUPERVISOR")
//            .and()
//            .withUser("cosup1").password("{noop}cos1pass").roles("CO_SUPERVISOR")
//            .and()
//            .withUser("hod1").password("{noop}hodpass").roles("HOD")
//            .and()
//            .withUser("admin").password("{noop}adminpass").roles("ADMIN");
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
          .csrf().disable() // disable for demo; enable & configure CSRF for browser forms in prod
          .authorizeRequests()
             // public endpoints
             .antMatchers("/api/progress/**").hasAuthority("SCHOLAR")
             .antMatchers("/api/documents/**").hasAuthority("SCHOLAR")
             .antMatchers("/api/progress-reports", "/api/progress-reports/**").permitAll()
             .antMatchers("/api/topics", "/api/topics/**").permitAll()
             // remarks list is public; adding remark restricted by method-level @PreAuthorize
             .antMatchers("/api/remarks").permitAll()
           
             .anyRequest().authenticated()
             .and()
             .sessionManagement()
                 .sessionCreationPolicy(SessionCreationPolicy.STATELESS);;
        //  .and()
         //    .httpBasic();
    }
}
