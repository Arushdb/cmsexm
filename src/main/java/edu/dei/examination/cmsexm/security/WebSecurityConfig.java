package edu.dei.examination.cmsexm.security;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import edu.dei.examination.cmsexm.security.jwt.AuthEntryPointJwt;
import edu.dei.examination.cmsexm.security.jwt.AuthTokenFilter;
import edu.dei.examination.cmsexm.service.UserDetailsServiceImpl;


@Configuration
@EnableWebSecurity
@EnableScheduling
@Order(1)
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		
				
	}
	
//	@Bean
//	public CorsFilter corsFilter() {
//		
//		CorsConfiguration corsConfiguration = new CorsConfiguration();
//		corsConfiguration.setAllowCredentials(true);
//		corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
//		
//		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin","Access-Control-Allow-Origin","Content-Type",
//				"Accept","Authorization","Origin","Accept","X-Requested-With"));
//		
//		corsConfiguration.setExposedHeaders(Arrays.asList("Origin","Content-Type","Accept",
//				"Authorization","Access-Control-Allow-Origin","Access-Control-Allow-Credentials"));
//		
//		corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
//		
//		org.springframework.web.cors.UrlBasedCorsConfigurationSource thesource = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
//				thesource.registerCorsConfiguration("/**", corsConfiguration);
//		//theurlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
//		return new CorsFilter(thesource);
//	}
	
	
	 @Bean
	    public CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration config = new CorsConfiguration();
	        config.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // Angular
	        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	        config.setAllowedHeaders(Arrays.asList("*"));
	        config.setAllowCredentials(true); // if using cookies / Authorization header
	        // 🔥 MOST IMPORTANT — Without this you won't see cookies
	        config.setExposedHeaders(Arrays.asList("Set-Cookie"));

	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", config);  // apply to all paths
	        return source;
	    }
	 
	 @Bean
	    public RestTemplate restTemplate() {
	        return new RestTemplate();
	    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
		//http.cors().and()
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authorizeRequests()
			.antMatchers("/api/scholars/**").hasAnyAuthority("ADMIN","SCHOLAR")
			.antMatchers("/api/phd/**").permitAll()
			.antMatchers("/api/auth/**").permitAll()
			.antMatchers("/api/test/**").permitAll() ;
			//.anyRequest().authenticated();
		/////////////////////////////////////////////////////////////////////////////////////////
//			.and()
//			.formLogin()
//			.loginPage("/login")
//			.permitAll()
//			.and()
//			.logout()
//				
//			.logoutSuccessHandler(new LogoutSuccessHandler() {
//				
//				@Override
//				public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
//						throws IOException, ServletException {
//					// TODO Auto-generated method stub
//					System.out.println("The User "+authentication.getName()+ "has loggedout ");
//					UrlPathHelper helper = new UrlPathHelper();
//					String context = helper.getContextPath(request);
//					response.sendRedirect(context+"/");
//					
//				}
//			}).permitAll();
		
		

		
//		http
//		.authorizeRequests()
//			.antMatchers("/", "/home").permitAll()
//			.anyRequest().authenticated()
//			.and()
//		.formLogin()
//						
//			.and()
//		.logout()
//		     .logoutSuccessHandler(new LogoutSuccessHandler() {
//				
//				@Override
//				public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
//						throws IOException, ServletException {
//					System.out.println("The User "+authentication.getName()+ "has logged out ");
//					UrlPathHelper helper = new UrlPathHelper();
//					String context = helper.getContextPath(request);
//					System.out.println("Context :"+context);
//					response.sendRedirect(context+"/"+"login");
//				}
//			})
//			.permitAll();
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
}
