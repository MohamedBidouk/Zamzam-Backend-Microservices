package org.zamzam.deliveryservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryPersonResponse {
    private String name;
    private String phoneNumber;
    private String vehiculeNumber;
}
