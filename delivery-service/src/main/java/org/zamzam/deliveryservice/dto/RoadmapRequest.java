package org.zamzam.deliveryservice.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.zamzam.deliveryservice.model.DeliveryPerson;

import java.util.List;

@Data
@NoArgsConstructor
public class RoadmapRequest {
    private DeliveryPerson deliveryPerson;
    private List<String> orderList;
}
