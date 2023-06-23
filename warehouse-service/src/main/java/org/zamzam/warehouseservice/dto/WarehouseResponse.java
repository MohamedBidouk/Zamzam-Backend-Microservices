package org.zamzam.warehouseservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseResponse {
    private Long id;
    private Integer currentCapacity;
    private Integer maxCapacity;
    private String streetAddress;
    private String city;
    private String state;
}
