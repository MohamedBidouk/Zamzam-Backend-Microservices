package org.zamzam.orderservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zamzam.orderservice.dto.OrderRequest;
import org.zamzam.orderservice.model.Order;
import org.zamzam.orderservice.model.OrderStatus;
import org.zamzam.orderservice.service.OrderService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        orderService.placeOrder(orderRequest);
        return "order placed successfully";
    }
    @GetMapping
    public ResponseEntity<List<Order>> getOrders(@RequestParam(required = false) OrderStatus status,
                                                 @RequestParam(required = false) Date startDate,
                                                 @RequestParam(required = false) Date endDate){
        List<Order> orders = orderService.getOrders(status, startDate, endDate);
        if (orders.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

}
