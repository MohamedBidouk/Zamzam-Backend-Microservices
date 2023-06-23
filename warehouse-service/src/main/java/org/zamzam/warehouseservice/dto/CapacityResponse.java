package org.zamzam.warehouseservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CapacityResponse {
    private Long warehouseId;
    private Integer currentCapacity;
    private Integer maxCapacity;
}
