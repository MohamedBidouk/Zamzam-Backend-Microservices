package org.zamzam.customerservice.dto;

import org.zamzam.customerservice.model.ConsumerCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerRequest {
    private String name;
    private ConsumerCategory consumerCategory;
    private String passport;
    private String enterDate;
    private String exitDate;
    private Integer quota;
}
