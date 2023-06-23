package org.zamzam.customerservice.controller;

import org.zamzam.customerservice.dto.ConsumerRequest;
import org.zamzam.customerservice.dto.ConsumerResponse;
import org.zamzam.customerservice.model.Consumer;
import org.zamzam.customerservice.repository.ConsumerRepository;
import org.zamzam.customerservice.service.ConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consumers")
@RequiredArgsConstructor
public class ConsumerController {
    private final ConsumerService consumerService;
    private final ConsumerRepository consumerRepository;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createConsumer(@RequestBody ConsumerRequest consumerRequest){
        consumerService.createConsumer(consumerRequest);
    }
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ConsumerResponse> getAllConsumer(){
        return consumerService.getAllConsumers();
    }
    @GetMapping("/quota/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Integer getConsumerQuota(@PathVariable("id")String id){
        return consumerService.getConsumerQuota(id);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Consumer> updateConsumer(@PathVariable("id")String id, @RequestBody ConsumerRequest consumerRequest){
        if (!consumerRepository.existsById(id)){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(consumerService.updateConsumer(id, consumerRequest), HttpStatus.OK);
    }
}
