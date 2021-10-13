package com.example.letter.lettertemplate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.File;

@SpringBootApplication
public class LettertemplateApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(LettertemplateApplication.class, args);
	}

	@Bean
	public MultipartResolver multipartResolver(){
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setMaxUploadSize(3661053);
		return resolver;
	}

	@Override
	public void run(String... args) throws Exception {
		File convFile = new File("/upload/images/");
		convFile.mkdirs();
		System.out.println("directory created "+convFile.exists());
	}
}
