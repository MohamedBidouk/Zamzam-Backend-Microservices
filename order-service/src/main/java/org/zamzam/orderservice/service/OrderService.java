package org.zamzam.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.zamzam.orderservice.dto.OrderRequest;
import org.zamzam.orderservice.event.OrderPlacedEvent;
import org.zamzam.orderservice.model.Order;
import org.zamzam.orderservice.model.OrderStatus;
import org.zamzam.orderservice.repository.OrderRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    private final KafkaTemplate<String, String> stringKafkaTemplate;
    public void sendMessage(String msg){
        CompletableFuture<SendResult<String, String>> future = stringKafkaTemplate
                .send("messageTopic", msg);
        future.whenComplete((result, ex) -> {
            if (ex == null ){
                System.out.println("Sent message=[" + msg +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }else {
                System.out.println("Unable to send message=[" +
                        msg + "] due to : " + ex.getMessage());
            }
        });
    }
    public void sendNotification(OrderPlacedEvent order){
        CompletableFuture<SendResult<String, OrderPlacedEvent>> future = kafkaTemplate
                .send("notificationTopic", order);
        future.whenComplete((result, ex) -> {
            if (ex == null ){
                System.out.println("Sent message=[" + order.getOrderNumber() +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }else {
                System.out.println("Unable to send message=[" +
                        order.getOrderNumber() + "] due to : " + ex.getMessage());
            }
        });

    }

    public void placeOrder(OrderRequest orderRequest){
        Order order= new Order();
        order.setId(orderRequest.getId());

        order.setOrderNumber(UUID.randomUUID().toString());
        order.setStatus(OrderStatus.PENDING);

        order.setQuantity(orderRequest.getQuantity());
        order.setSenderOrganizationId(orderRequest.getSenderOrganizationId());
        order.setReceiverOrganizationId(orderRequest.getReceiverOrganizationId());
        order.setReceiverDepotId(orderRequest.getReceiverDepotId());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", orderRequest.getReceiverDepotId().toString());
        params.add("quantity", orderRequest.getQuantity().toString());

        //I will check if the depot capacity can load the order
        Boolean isAccepted = webClientBuilder.build().get()
                .uri("http://depot-service/api/depots/loadRequest",
                        uriBuilder -> uriBuilder.queryParams(params).build())
                .retrieve()
                .bodyToMono(boolean.class)
                .block();
        if (Boolean.TRUE.equals(isAccepted)){
            orderRepository.save(order);
            sendNotification(new OrderPlacedEvent(order.getId(), order.getOrderNumber(), order.getSenderOrganizationId(), order.getReceiverOrganizationId(), order.getReceiverDepotId(),  order.getQuantity()));
        }else {
            throw new IllegalArgumentException("Can't accept this quantity");
        }
    }

    public List<Order> getOrders(OrderStatus status, Date startDate, Date endDate) {
        sendMessage("Someone get all orders");
        if (status == null && startDate == null && endDate == null) {
            return orderRepository.findAll();
        } else if (status != null && startDate == null && endDate == null) {
            return orderRepository.findByStatus(status);
        } else if (status == null && startDate != null && endDate != null) {
            return orderRepository.findByCreatedAtBetween(startDate, endDate);
        } else {
            return orderRepository.findByStatusAndCreatedAtBetween(status, startDate, endDate);
        }
    }
}
