package org.zamzam.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zamzam.orderservice.model.Order;
import org.zamzam.orderservice.model.OrderStatus;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus orderStatus);
    List<Order> findByCreatedAtBetween(Date startDate, Date endDate);
    List<Order> findByStatusAndCreatedAtBetween(OrderStatus orderStatus, Date startDate, Date endDate);
    List<Order> findBySenderOrganizationId(String senderOrganizationId);
}
