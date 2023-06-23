package org.zamzam.refrigeratorservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.zamzam.refrigeratorservice.model.Refrigerator;

import java.util.List;

public interface RefrigeratorRepository extends JpaRepository<Refrigerator, Long> {
    boolean existsByOrganizationId(String organizationId);
    List<Refrigerator> findByOrganizationId(String organizationId);
    void deleteAllByOrganizationId(String organizationId);
    boolean existsBySerialNumber(String serialNumber);
}
