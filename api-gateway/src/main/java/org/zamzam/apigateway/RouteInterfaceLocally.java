package org.zamzam.apigateway;

public interface RouteInterfaceLocally {
    public static final String refrigeratorServiceName = "refrigerator-service";
    public static final String refrigeratorServiceUri = "lb://refrigerator-service";
    public static final String refrigeratorServicePredication = "Path=/api/refrigerator/**";
    public static final String warehouseServiceName = "warehouse-service";
    public static final String warehouseServiceUri = "lb://warehouse-service";
    public static final String warehouseServicePredication = "Path=/api/warehouse/**";
    public static final String orderServiceName = "order-service";
    public static final String orderServiceUri = "lb://order-service";
    public static final String orderServicePredication = "Path=/api/order/**";
    public static final String consumeServiceName = "consumption-service";
    public static final String consumeServiceUri = "lb://consumption-service";
    public static final String consumeServicePredication = "Path=/api/consume/**";
    public static final String customerServiceName = "customer-service";
    public static final String customerServiceUri = "lb://customer-service";
    public static final String customerServicePredication = "Path=/api/customer/**";
    public static final String organizationServiceName = "organization-service";
    public static final String organizationServiceUri = "lb://organization-service";
    public static final String organizationServicePredication = "Path=/api/organization/**";
    public static final String deliveryServiceName = "delivery-service";
    public static final String deliveryServiceUri = "lb://delivery-service";
    public static final String deliveryServicePredication = "Path=/api/delivery/**";
}
