package com.example.demo;

import java.util.Map;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
	
	@Bean
	public InfoContributor appInfoContributor() {
		InfoContributor infoContributor = new InfoContributor() {
			@Override
			public void contribute(Info.Builder builder) {
				//builder.withDetail("app", "My Welcome App");
				builder.withDetail("app", 
						Map.of(
							"name", "My Welcome App",
							"description", "Welcome to my app",
	                        "version", "1.0.0",
	                        "last_updated", "2024-02-25"
                        ));
				builder.withDetail("service", "My Service App");
			}
		};
		
		return infoContributor;
	}
	
}
