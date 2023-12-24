package com.finnoapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.finnoapp.pojos.ImageUpload;

@Configuration
public class BeanConfiguration {

	@Bean
	public ImageUpload getImageUpload() {
		return new ImageUpload();
	}

}
