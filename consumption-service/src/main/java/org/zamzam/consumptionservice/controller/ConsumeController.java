package org.zamzam.consumptionservice.controller;


import org.springframework.web.bind.annotation.*;
import org.zamzam.consumptionservice.dto.ConsumeRequest;
import org.zamzam.consumptionservice.dto.ConsumeResponse;
import org.zamzam.consumptionservice.exception.BadRequestException;
import org.zamzam.consumptionservice.service.ConsumeService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/api/consume")
@RequiredArgsConstructor
public class ConsumeController {
    private final ConsumeService consumeService;
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @CircuitBreaker(name = "consume", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "consume")
    @Retry(name = "consume")
    public CompletableFuture<ResponseEntity<ConsumeResponse>> createConsumption(@RequestBody ConsumeRequest consumeRequest){
        return CompletableFuture.supplyAsync(()->consumeService.CreateConsumption(consumeRequest));
    }
    public CompletableFuture<String> fallbackMethod(ConsumeRequest consumeRequest, RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(()->"oops something went wrong");
    }
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ConsumeResponse> getAllConsumption(){
        return consumeService.getAllConsumptions();
    }
    @GetMapping("/consumers/{consumerId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ConsumeResponse> getAllConsumptionByConsumerId(@PathVariable("consumerId")String consumerId,
                                                               @RequestParam(required = false)String dateOfStart,
                                                               @RequestParam(required = false)String dateOfEnd){
        if (!consumeService.existsByConsumerId(consumerId)){
            throw new RuntimeException("Not found consumer with id = "+consumerId);
        }
        if (dateOfStart != null && dateOfEnd != null){
            return consumeService.getConsumeListPerDay(consumerId, dateOfStart, dateOfEnd);
        }
        return consumeService.getAllConsumptionByConsumerId(consumerId);
    }
    @GetMapping("/consumes/refrigerators/{refrigeratorId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ConsumeResponse> getConsumesByRefrigeratorId(@PathVariable("refrigeratorId") Long refrigeratorId,
                                                             @RequestParam(required = false) String dateOfStart,
                                                             @RequestParam(required = false) String dateOfEnd){
        List<ConsumeResponse> consumeResponseList ;

        if (dateOfStart != null && dateOfEnd != null){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date dateS = formatter.parse(dateOfStart);
                Date dateE = formatter.parse(dateOfEnd);
                consumeResponseList = consumeService.getConsumeListByRefrigeratorIdAndDateBetween(refrigeratorId, dateS, dateE);
            } catch (ParseException e) {
                throw new BadRequestException("Put a valid date with format 'yyyy-MM-dd'");
            }
        }else {
            consumeResponseList = consumeService.getConsumeListByRefrigeratorId(refrigeratorId);
        }

        if (consumeResponseList == null){
            throw  new BadRequestException("Not exist refrigerator with id = "+ refrigeratorId);
        }
        return consumeResponseList;
    }
}
