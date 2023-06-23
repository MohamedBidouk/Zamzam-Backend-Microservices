package org.zamzam.consumptionservice.service;

import org.zamzam.consumptionservice.dto.ConsumeRequest;
import org.zamzam.consumptionservice.dto.ConsumeResponse;
import org.zamzam.consumptionservice.dto.UpdateRequest;
import org.zamzam.consumptionservice.exception.ReachedDailyLimitException;
import org.zamzam.consumptionservice.exception.RefrigeratorCanNotHandleRequestException;
import org.zamzam.consumptionservice.model.Consume;
import org.zamzam.consumptionservice.repository.ConsumeRepository;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumeService {
    private final ConsumeRepository consumeRepository;
    private final WebClient.Builder webClient;
    private final Tracer tracer;
    public boolean existsByConsumerId(String consumerId){
        return consumeRepository.existsByConsumerId(consumerId);
    }
    public ResponseEntity<ConsumeResponse> CreateConsumption(ConsumeRequest consumeRequest){
        Consume consume = new Consume();
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setRefrigeratorId(consumeRequest.getRefrigeratorId());
        updateRequest.setQuantity(consumeRequest.getQuantity());
        log.info("it start to try");

        if (!isEligible(getConsumerQuota(consumeRequest.getConsumerId()), consumedToday(consumeRequest.getConsumerId()))){
            log.info("You reach your daily limit");
            throw new ReachedDailyLimitException("You reach your daily limit") ;
        } else {
            log.info("You don't reach your daily limit");
        }

        if (!canRefrigeratorHandleRequest(updateRequest)){
            log.info("Insufficient storage in refrigerator !");
            throw new RefrigeratorCanNotHandleRequestException("Insufficient storage in refrigerator !");
        }

        consume.setRefrigeratorId(consumeRequest.getRefrigeratorId());
        consume.setQuantity(consumeRequest.getQuantity());
        consume.setConsumerId(consumeRequest.getConsumerId());
        ConsumeResponse response = mapToDto(consumeRepository.save(consume));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    public List<ConsumeResponse> getConsumeListByRefrigeratorId(Long refrigeratorId){
        if (!consumeRepository.existsByRefrigeratorId(refrigeratorId)){
            return null;
        }
        return consumeRepository.findByRefrigeratorId(refrigeratorId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    public List<ConsumeResponse> getConsumeListByRefrigeratorIdAndDateBetween(Long refrigeratorId,
                                                                              Date dateOfStart,
                                                                              Date dateOfEnd){
        if (!consumeRepository.existsByRefrigeratorId(refrigeratorId)){
            return null;
        }
        return consumeRepository.findByRefrigeratorIdAndConsumptionTimeBetween(refrigeratorId, dateOfStart, dateOfEnd)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    boolean isEligible(Integer quota, Integer consumed){
        return consumed<quota;
    }

    Integer getConsumerQuota(String consumerId){
        tracer.nextSpan().name("Request quota from Consumer-service");
        String consumersUrl = "http://consumer-service/api/consumers/quota/";
        return  webClient.build().get()
                .uri(String.join("", consumersUrl, consumerId))
                .retrieve()
                .bodyToMono(Integer.class)
                .onErrorResume(error -> Mono.just(4))
                .block();
    }

    boolean canRefrigeratorHandleRequest(UpdateRequest updateRequest){
        tracer.nextSpan().name("Request Refrigerator eligibility from Refrigerator-service");
        String refrigeratorServiceUrl = "http://refrigerator-service/api/refrigerators/canUnload/";
        return Boolean.TRUE.equals(webClient.build().get()
                .uri(refrigeratorServiceUrl+updateRequest.getRefrigeratorId()+"/"+updateRequest.getQuantity())
                .retrieve()
                .bodyToMono(boolean.class)
                .block());
    }

    public Date add24Hour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 24);
        return calendar.getTime();
    }
    public Integer consumedToday(String consumerId){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String todayLoc = LocalDate.now().toString();
        Integer consumed = 0;
        try {
            Date today = formatter.parse(todayLoc);
            System.out.println(today);
            List<Consume> consumeList =
                    consumeRepository.findByConsumerIdAndConsumptionTimeBetween(consumerId, today, add24Hour(today));
            for (Consume consume : consumeList) {
                consumed += consume.getQuantity();
            }
            return consumed;
        } catch (ParseException e) {
            throw new RuntimeException("Put a valid date with format 'yyyyy-MM-dd");
        }
    }
    public List<ConsumeResponse> getConsumeListPerDay(String consumerId, String dateOfStart, String dateOfEnd){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date dateS ;
        Date dateE ;

        try {
            dateS = formatter.parse(dateOfStart);
            dateE = formatter.parse(dateOfEnd);
        } catch (ParseException e) {
            throw new RuntimeException("Put a valid date with format 'yyyyy-MM-dd");
        }

        return consumeRepository.findByConsumerIdAndConsumptionTimeBetween(consumerId, dateS, dateE)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    public List<ConsumeResponse> getAllConsumptionByConsumerId(String consumerId){
        return consumeRepository.findByConsumerId(consumerId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }

    private ConsumeResponse mapToDto(Consume consume) {
        return ConsumeResponse.builder()
                .consumerId(consume.getConsumerId())
                .consumptionTime(consume.getConsumptionTime())
                .refrigeratorId(consume.getRefrigeratorId())
                .quantity(consume.getQuantity())
                .build();
    }

    public List<ConsumeResponse> getAllConsumptions() {
        return consumeRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    @ExceptionHandler(ReachedDailyLimitException.class)
    public ResponseEntity<String> handleReachedDailyLimit(ReachedDailyLimitException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(RefrigeratorCanNotHandleRequestException.class)
    public ResponseEntity<String> handleRefrigeratorCanNotHandleRequest(RefrigeratorCanNotHandleRequestException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
