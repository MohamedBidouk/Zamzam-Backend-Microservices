package org.zamzam.deliveryservice.services;

import org.springframework.http.ResponseEntity;
import org.zamzam.deliveryservice.dto.RoadmapRequest;
import org.zamzam.deliveryservice.dto.RoadmapResponse;
import org.zamzam.deliveryservice.model.Order;

import java.util.List;

public interface RoadmapService {
    RoadmapResponse saveRoadmap(RoadmapRequest roadmapRequest);
    List<RoadmapResponse> findByDeliveryPersonId(Long id);
    List<RoadmapResponse> findByDeliveryPersonName(String name);
    boolean existsByDeliveryPersonId(Long deliveryPersonId);

    List<RoadmapResponse> findAll();

    List<Order> getAllOrders();

    RoadmapResponse getById(Long roadmapId);
    void deleteAll();

    List<Order> getPendingOrders(boolean isDelivered);

    ResponseEntity updateRoadmap(Long roadmapId, RoadmapRequest roadmapRequest);
}
