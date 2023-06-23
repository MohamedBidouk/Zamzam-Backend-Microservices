package org.zamzam.deliveryservice.dto;

import lombok.Builder;
import lombok.Data;
import org.zamzam.deliveryservice.model.Order;

import java.util.Date;
import java.util.List;
@Data
@Builder
public class RoadmapResponse {
    private Long id;
    private Date date;
    private DeliveryPersonResponse deliveryPerson;
    private List<Order> orderList;
    private Integer totalOrders;
    private boolean isDelivered;
}
