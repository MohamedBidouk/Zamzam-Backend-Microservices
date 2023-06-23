package org.zamzam.warehouseservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseRequest {
    private String organizationId;
    private Integer currentCapacity;
    private Integer maxCapacity;
    private String streetAddress;
    private String city;
    private String state;
}
