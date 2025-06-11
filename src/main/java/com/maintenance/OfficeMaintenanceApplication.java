package com.maintenance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OfficeMaintenanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OfficeMaintenanceApplication.class, args);
    }
} 