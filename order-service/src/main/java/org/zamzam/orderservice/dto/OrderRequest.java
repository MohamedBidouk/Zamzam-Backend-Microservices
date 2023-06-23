package org.zamzam.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zamzam.orderservice.model.OrderStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private Long id;
    private String orderNumber;
    private String senderOrganizationId;
    private String receiverOrganizationId;
    private Long receiverDepotId;
    private Integer quantity;
    private OrderStatus status;
}
