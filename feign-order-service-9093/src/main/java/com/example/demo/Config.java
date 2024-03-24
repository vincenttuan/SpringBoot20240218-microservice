package com.example.demo;

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
				builder.withDetail("app", "My Order App");
			}
		};
		
		return infoContributor;
	}
	
}
