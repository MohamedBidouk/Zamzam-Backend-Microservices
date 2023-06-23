package org.zamzam.refrigeratorservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefrigeratorRequest {
    private String organizationId;
    private String serialNumber;
    private Integer currentCapacity;
    private Integer maxCapacity;
    private String address;
}
