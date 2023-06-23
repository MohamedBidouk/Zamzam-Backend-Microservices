package org.zamzam.customerservice.repository;

import org.zamzam.customerservice.model.Consumer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConsumerRepository extends MongoRepository<Consumer, String> {
}
