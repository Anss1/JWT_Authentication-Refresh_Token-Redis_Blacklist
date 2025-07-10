package com.anas.jwtSecurityTemplate.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class DotenvInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory("./")
                    .ignoreIfMissing()
                    .load();

            Map<String, Object> map = new HashMap<>();
            dotenv.entries().forEach(entry -> {
                map.put(entry.getKey(), entry.getValue());
                System.out.println("Loaded: " + entry.getKey() + " = " + entry.getValue());
            });

            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            environment.getPropertySources().addFirst(new MapPropertySource("dotenvProperties", map));

            System.out.println("✅ .env variables loaded successfully");
        } catch (Exception e) {
            System.err.println("❌ Failed to load .env file: " + e.getMessage());
        }
    }
}