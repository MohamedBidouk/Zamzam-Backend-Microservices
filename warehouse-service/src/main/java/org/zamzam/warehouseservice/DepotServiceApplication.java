package org.zamzam.warehouseservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class DepotServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DepotServiceApplication.class, args);
    }

}
