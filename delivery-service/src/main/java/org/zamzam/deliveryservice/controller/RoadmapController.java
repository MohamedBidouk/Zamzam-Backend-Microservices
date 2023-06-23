package org.zamzam.deliveryservice.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zamzam.deliveryservice.dto.RoadmapRequest;
import org.zamzam.deliveryservice.dto.RoadmapResponse;
import org.zamzam.deliveryservice.model.Order;
import org.zamzam.deliveryservice.services.RoadmapService;

import java.util.List;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class RoadmapController {
    private final RoadmapService roadmapService;
    @GetMapping()
    public ResponseEntity<String> test(){
        return new ResponseEntity<>("I'm worked", HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<RoadmapResponse> createRoadmap(@RequestBody RoadmapRequest roadmapRequest){
        System.out.println(roadmapRequest);
        return new ResponseEntity<>(roadmapService.saveRoadmap(roadmapRequest), HttpStatus.CREATED);
    }
    @GetMapping("/all")
    public ResponseEntity<List<RoadmapResponse>> getAllRoadmaps(@RequestParam(required = false) String name){
        List<RoadmapResponse> responseList ;
        if (name != null){
            responseList = roadmapService.findByDeliveryPersonName(name);
            if (responseList != null){
                return new ResponseEntity<>(roadmapService.findByDeliveryPersonName(name), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        responseList = roadmapService.findAll();
        if (responseList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
    @GetMapping("/orders/all")
    public ResponseEntity<List<Order>> getAllOrders(){
        List<Order> orders = roadmapService.getAllOrders();
        if (orders.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getPendingOrders(@RequestParam(name = "isDelivered", defaultValue = "false")boolean isDelivered){
        List<Order> orders;
        if (isDelivered){
         orders = roadmapService.getPendingOrders(true);
        }else {
            orders = roadmapService.getPendingOrders(false);
        }
        if (orders.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    @GetMapping("/{roadmapId}")
    public ResponseEntity<RoadmapResponse> getRoadmapById(@PathVariable("roadmapId")Long roadmapId){
        RoadmapResponse roadmap = roadmapService.getById(roadmapId);
        if (roadmap == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(roadmap, HttpStatus.OK);
    }
    @PutMapping("/{roadmapId}")
    public ResponseEntity<RoadmapResponse> updateRoadmap(@PathVariable("roadmapId")Long roadmapId,
                                                         @RequestBody RoadmapRequest roadmapRequest){

        return roadmapService.updateRoadmap(roadmapId, roadmapRequest);
    }

}
