package org.zamzam.refrigeratorservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefrigeratorResponse {
    private Long id;
    private String serialNumber;
    private Integer currentCapacity;
    private Integer maxCapacity;
    private String address;
}
