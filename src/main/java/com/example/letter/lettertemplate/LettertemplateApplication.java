package com.example.letter.lettertemplate;

import com.mongodb.client.MongoClient;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.File;

@SpringBootApplication
public class LettertemplateApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(LettertemplateApplication.class, args);
	}

//	@Bean
//	public ModelMapper modelMapper(){
//		ModelMapper mapper = new ModelMapper();
//		return mapper;
//	}

	@Bean
	public MultipartResolver multipartResolver(){
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setMaxUploadSize(3661053);
		return resolver;
	}

//	@Bean("cmongoTemplate")
//	public MongoTemplate getTemplate(){
//
//		try{
//			MongoClientFactoryBean factoryBean = new MongoClientFactoryBean();
//			factoryBean.setHost("localhost");
//			factoryBean.setPort(27017);
//			factoryBean.afterPropertiesSet();
//			MongoClient client = factoryBean.getObject();
//			SimpleMongoClientDatabaseFactory databaseFactory= new SimpleMongoClientDatabaseFactory(client,"letter_template_base");
//
//			MongoTemplate template = new MongoTemplate(databaseFactory);
//			return template;
//		}catch(Exception e) {
//			System.out.println(e);
//			System.exit(1);
//		}
//		return null;
//	}

	@Override
	public void run(String... args) throws Exception {
		File convFile = new File("/templates/");
		convFile.mkdirs();
		System.out.println("directory created "+convFile.exists());
	}



}
