package edu.dei.examination.cmsexm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class CmsexmApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(CmsexmApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		
		return builder.sources(CmsexmApplication.class);
	
	}
}
