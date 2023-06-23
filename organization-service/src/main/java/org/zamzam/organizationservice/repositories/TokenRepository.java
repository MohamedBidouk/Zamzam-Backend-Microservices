package org.zamzam.organizationservice.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.zamzam.organizationservice.model.ValidationToken;

public interface TokenRepository extends JpaRepository<ValidationToken, String> {
}
