package org.zamzam.notificationservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notificationTopic", groupId = "notificationId", containerFactory = "orderKafkaListenerContainerFactory")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent){
        log.info("Received notification for order number = {}", orderPlacedEvent.getOrderNumber() );
        log.info("Received notification for order quantity = {}", orderPlacedEvent.getQuantity() );
    }

}