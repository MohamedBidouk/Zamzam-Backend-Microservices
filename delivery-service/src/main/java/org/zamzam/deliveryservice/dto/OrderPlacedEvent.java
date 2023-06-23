package org.zamzam.deliveryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlacedEvent {
    private Long id;
    private String orderNumber;
    private String senderOrganizationId;
    private String receiverOrganizationId;
    private Long receiverDepotId;
    private Integer quantity;
}
