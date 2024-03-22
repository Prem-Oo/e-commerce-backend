//package com.shopping.config;
//
//import java.util.Collections;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//
//	 @Bean
//	    public Docket api() { 
//	        return new Docket(DocumentationType.SWAGGER_2)  
//	          .select()                                  
//	          .apis(RequestHandlerSelectors.basePackage("com.shopping.controller"))              
//	          .paths(PathSelectors.regex("/api/shoppingservice.*"))                          
//	          .build()
//	          .apiInfo(getApiInfo());
//	    }
//	 private ApiInfo getApiInfo() {
//		 Contact contact = new Contact("nitin", "http://www.ineuron.ai/course",
//
//		 "nitin@ineuron.ai@gmail.com");
//
//		 return new ApiInfo("TouristInfo",
//
//		 "Gives information about tourist activities",
//		 "3.4.RELEASE",
//		 "http:www.hcl.com/license",
//		 contact,
//		 "GNU PUBLIC",
//		 "http://apache.org/license/guru",
//		 Collections.emptyList());
//
//		 }
//}
