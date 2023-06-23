package org.zamzam.consumptionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsumeResponse {
    private Date consumptionTime;
    private String consumerId;
    private Long refrigeratorId;
    private Integer quantity;
}
