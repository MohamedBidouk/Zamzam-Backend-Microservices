package org.zamzam.customerservice.service;

import org.zamzam.customerservice.dto.ConsumerRequest;
import org.zamzam.customerservice.dto.ConsumerResponse;
import org.zamzam.customerservice.model.Consumer;
import org.zamzam.customerservice.repository.ConsumerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ConsumerService {
    private final ConsumerRepository consumerRepository;
    public void createConsumer(ConsumerRequest consumerRequest){
        int inputQuota ;
        if (consumerRequest.getQuota() != null){
            inputQuota = consumerRequest.getQuota();
        }else {
            inputQuota = 4;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date enterDate = formatter.parse(consumerRequest.getEnterDate());
            Date exitDate = formatter.parse(consumerRequest.getExitDate());
            Consumer consumer = Consumer.builder()
                    .name(consumerRequest.getName())
                    .consumerCategory(consumerRequest.getConsumerCategory())
                    .passport(consumerRequest.getPassport())
                    .quota(inputQuota)
                    .enterDate(enterDate)
                    .exitDate(exitDate)
                    .build();

            consumerRepository.save(consumer);

            log.info("Consumer {} is saved", consumer.getId());
        } catch (ParseException e) {
            throw new RuntimeException("Put an valid date format : 'yyyy-MM-dd'");
        }
    }
    public Consumer updateConsumer(String id, ConsumerRequest consumerRequest){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Consumer consumer = consumerRepository.findById(id).get();
        consumer.setName(consumerRequest.getName());
        consumer.setConsumerCategory(consumerRequest.getConsumerCategory());
        consumer.setQuota(consumerRequest.getQuota());
        consumer.setPassport(consumerRequest.getPassport());
        try {
            consumer.setEnterDate(formatter.parse(consumerRequest.getEnterDate()));
            consumer.setExitDate(formatter.parse(consumerRequest.getExitDate()));
        } catch (ParseException e) {
            throw new RuntimeException("Put an valid date format : 'yyyy-MM-dd'");
        }

        log.info("Consumer {} is updated", consumer.getId());
        return consumerRepository.save(consumer);
    }
    public Integer getConsumerQuota(String consumerId){
        return consumerRepository.findById(consumerId).get().getQuota();
    }

    public List<ConsumerResponse> getAllConsumers() {
        List<Consumer> consumerList =  consumerRepository.findAll();

        return consumerList.stream()
                .map(this::mapToConsumerResponse)
                .toList();
    }

    private ConsumerResponse mapToConsumerResponse(Consumer consumer) {
        return ConsumerResponse.builder()
                .id(consumer.getId())
                .name(consumer.getName())
                .passport(consumer.getPassport())
                .consumerCategory(consumer.getConsumerCategory())
                .enterDate(consumer.getEnterDate())
                .exitDate(consumer.getExitDate())
                .quota(consumer.getQuota())
                .build();
    }
}
