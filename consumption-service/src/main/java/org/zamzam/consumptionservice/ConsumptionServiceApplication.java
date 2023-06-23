package org.zamzam.consumptionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ConsumptionServiceApplication {
    public static void main(String[] args){
        SpringApplication.run(ConsumptionServiceApplication.class, args);
    }
}
