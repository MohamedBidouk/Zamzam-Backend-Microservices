package org.zamzam.warehouseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zamzam.warehouseservice.model.Warehouse;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    boolean existsByOrganizationId(String organizationId);
    List<Warehouse> findByOrganizationId(String organizationId);
    void deleteAllByOrganizationId(String organizationId);
}
