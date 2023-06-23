package org.zamzam.deliveryservice.orderListner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.zamzam.deliveryservice.Repositories.OrderRepository;
import org.zamzam.deliveryservice.dto.OrderPlacedEvent;
import org.zamzam.deliveryservice.model.Order;

@Component
@RequiredArgsConstructor
@Slf4j
public class handelOrder {
    private final OrderRepository orderRepository;
    @KafkaListener(topics = "notificationTopic", groupId = "roadmapGroup", containerFactory = "orderKafkaListenerContainerFactory")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent){
        log.info("Received notification for order number = {}", orderPlacedEvent.getOrderNumber() );
        log.info("Received notification for order quantity = {}", orderPlacedEvent.getQuantity() );
        Order order = Order.builder()
                        .id(orderPlacedEvent.getId())
                                .orderNumber(orderPlacedEvent.getOrderNumber())
                                        .quantity(orderPlacedEvent.getQuantity())
                                                .receiverDepotId(orderPlacedEvent.getReceiverDepotId())
                                                        .senderOrganizationId(orderPlacedEvent.getSenderOrganizationId())
                                                                .receiverOrganizationId(orderPlacedEvent.getReceiverOrganizationId())
                                                                        .isDelivered(false)
                                                                                .build();
        Order savedOrder = orderRepository.save(order);
        log.info("saved {}", savedOrder);
    }
}
