package org.zamzam.customerservice.dto;

import org.zamzam.customerservice.model.ConsumerCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerResponse {
    private String id;
    private String name;
    private ConsumerCategory consumerCategory;
    private String passport;
    private Date enterDate;
    private Date exitDate;
    private Integer quota;
}
