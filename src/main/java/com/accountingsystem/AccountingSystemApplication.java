package com.accountingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.accountingsystem"})
public class AccountingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountingSystemApplication.class, args);
	}

}
