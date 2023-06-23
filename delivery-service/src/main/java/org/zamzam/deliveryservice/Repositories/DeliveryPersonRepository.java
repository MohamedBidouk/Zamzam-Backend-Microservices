package org.zamzam.deliveryservice.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zamzam.deliveryservice.model.DeliveryPerson;

import java.util.List;

@Repository
public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson, Long> {
    List<DeliveryPerson> findByNameContains(String name);
    boolean existsByName(String name);
}
