package org.zamzam.consumptionservice.repository;

import org.zamzam.consumptionservice.model.Consume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ConsumeRepository extends JpaRepository<Consume, Long> {
    List<Consume> findByConsumerId(String consumerId);
    List<Consume> findByConsumerIdAndConsumptionTimeBetween(String consumerId, Date start, Date end);
    boolean existsByConsumerId(String consumerId);
    boolean existsByRefrigeratorId(Long refrigeratorId);
    List<Consume> findByRefrigeratorId(Long refrigeratorId);
    List<Consume> findByRefrigeratorIdAndConsumptionTimeBetween(Long refrigeratorId, Date dateOfStart, Date dateOfEnd);
}
