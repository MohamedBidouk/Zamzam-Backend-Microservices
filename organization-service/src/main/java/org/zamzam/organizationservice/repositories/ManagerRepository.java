package org.zamzam.organizationservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.zamzam.organizationservice.model.Manager;

import java.util.List;

public interface ManagerRepository extends JpaRepository<Manager, String> {
    List<Manager> findByOrganizationId(String organizationId);
    @Transactional
    void deleteByOrganizationId(String organizationId);
}
