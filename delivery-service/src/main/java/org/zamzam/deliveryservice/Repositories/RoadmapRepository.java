package org.zamzam.deliveryservice.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zamzam.deliveryservice.model.Roadmap;

import java.util.List;

@Repository
public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    List<Roadmap> findByDeliveryPersonId(Long deliveryPersonId);
    boolean existsByDeliveryPersonId(Long deliveryPersonId);
    List<Roadmap> findByDeliveryPersonName(String name);
}
