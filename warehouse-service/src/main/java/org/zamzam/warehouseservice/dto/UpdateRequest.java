package org.zamzam.warehouseservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequest {
    private Long warehouseId;
    private Integer quantity;
}
