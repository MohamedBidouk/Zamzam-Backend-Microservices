package org.zamzam.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    public static void main(String[] args){
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
/*
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route(RouteInterfaceLocally.refrigeratorServiceName,
                        r -> r.remoteAddr(RouteInterfaceLocally.refrigeratorServiceUri)
                                .uri(RouteInterfaceLocally.refrigeratorServicePredication))
                .route(RouteInterfaceLocally.consumeServiceName,
                        r -> r.remoteAddr(RouteInterfaceLocally.consumeServiceUri)
                                .uri(RouteInterfaceLocally.consumeServicePredication))
                .route(RouteInterfaceLocally.deliveryServiceName,
                        r -> r.remoteAddr(RouteInterfaceLocally.deliveryServiceUri)
                                .uri(RouteInterfaceLocally.deliveryServicePredication))
                .route(RouteInterfaceLocally.warehouseServiceName,
                        r -> r.remoteAddr(RouteInterfaceLocally.warehouseServiceUri)
                                .uri(RouteInterfaceLocally.warehouseServicePredication))
                .route(RouteInterfaceLocally.orderServiceName,
                        r -> r.remoteAddr(RouteInterfaceLocally.orderServiceUri)
                                .uri(RouteInterfaceLocally.orderServicePredication))
                .route(RouteInterfaceLocally.customerServiceName,
                        r -> r.remoteAddr(RouteInterfaceLocally.customerServiceUri)
                                .uri(RouteInterfaceLocally.customerServicePredication))
                .route(RouteInterfaceLocally.organizationServiceName,
                        r -> r.remoteAddr(RouteInterfaceLocally.organizationServiceUri)
                                .uri(RouteInterfaceLocally.organizationServicePredication))
                .build();
    }*/

}
