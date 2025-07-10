package com.anas.jwtSecurityTemplate;

import com.anas.jwtSecurityTemplate.config.DotenvInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JwtSecurityTemplateApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(JwtSecurityTemplateApplication.class);
		app.addInitializers(new DotenvInitializer());
		app.run(args);
	}

}
