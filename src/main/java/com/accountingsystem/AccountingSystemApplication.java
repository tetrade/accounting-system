package com.accountingsystem;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = {"com.accountingsystem"})
@EnableMongoRepositories("com.accountingsystem")
public class AccountingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountingSystemApplication.class, args);
	}
}
