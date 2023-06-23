package org.zamzam.consumptionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumeRequest {
    private String consumerId;
    private Long refrigeratorId;
    private Integer quantity;
}
