package org.zamzam.organizationservice.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zamzam.organizationservice.model.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, String> {

}
