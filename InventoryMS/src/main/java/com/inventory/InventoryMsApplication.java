package com.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryMsApplication.class, args);
	}

}
