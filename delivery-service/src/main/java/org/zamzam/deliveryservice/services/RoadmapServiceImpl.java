package org.zamzam.deliveryservice.services;


import org.zamzam.deliveryservice.Repositories.OrderRepository;
import org.zamzam.deliveryservice.Repositories.RoadmapRepository;
import org.zamzam.deliveryservice.dto.DeliveryPersonResponse;
import org.zamzam.deliveryservice.dto.RoadmapRequest;
import org.zamzam.deliveryservice.dto.RoadmapResponse;
import org.zamzam.deliveryservice.exception.BadRequestException;
import org.zamzam.deliveryservice.exception.ResourceNotFoundException;
import org.zamzam.deliveryservice.model.DeliveryPerson;
import org.zamzam.deliveryservice.model.Order;
import org.zamzam.deliveryservice.model.Roadmap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zamzam.deliveryservice.Repositories.DeliveryPersonRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RoadmapServiceImpl implements RoadmapService{

    private final DeliveryPersonRepository deliveryPersonRepository;
    private final OrderRepository orderRepository;
    private final RoadmapRepository roadmapRepository;
    @Override
    public RoadmapResponse saveRoadmap(RoadmapRequest roadmapRequest){
        Roadmap roadmap = new Roadmap();
        roadmap.setDate(new Date());
        Roadmap _roadmap = roadmapRepository.save(roadmap);
        //orders treatment
        List<Order> savedOrders = saveOrdersFromRequest(roadmapRequest.getOrderList(), _roadmap);
        //deliveryPerson treatment
        DeliveryPerson deliveryPerson = treatRequestDeliveryPerson(roadmapRequest.getDeliveryPerson());
        deliveryPerson.getRoadmaps().add(_roadmap);
        DeliveryPerson _deliveryPerson = deliveryPersonRepository.save(deliveryPerson);
        //save roadmap
        _roadmap.setOrderList(savedOrders);
        _roadmap.setDeliveryPerson(_deliveryPerson);
        _roadmap.setTotalOrders(savedOrders.size());
        _roadmap.setDelivered(false);
        _roadmap = roadmapRepository.save(_roadmap);
        return mapToRoadmapResponse(_roadmap);

    }

    @Override
    public List<RoadmapResponse> findByDeliveryPersonId(Long deliveryPersonId) {
        if (existsByDeliveryPersonId(deliveryPersonId)){
            return roadmapRepository.findByDeliveryPersonId(deliveryPersonId)
                    .stream()
                    .map(this::mapToRoadmapResponse)
                    .collect(Collectors.toList());
        }else
            return null;
    }

    @Override
    public List<RoadmapResponse> findByDeliveryPersonName(String name) {
        if (deliveryPersonRepository.existsByName(name)){
            return roadmapRepository.findByDeliveryPersonName(name)
                    .stream()
                    .map(this::mapToRoadmapResponse)
                    .collect(Collectors.toList());
        }else
            return null;
    }

    @Override
    public boolean existsByDeliveryPersonId(Long deliveryPersonId) {
        return deliveryPersonRepository.existsById(deliveryPersonId);
    }

    @Override
    public List<RoadmapResponse> findAll() {
        return roadmapRepository.findAll()
                .stream()
                .map(this::mapToRoadmapResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public RoadmapResponse getById(Long roadmapId) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(()->new ResourceNotFoundException("Not found with id = "+ roadmapId));
        return mapToRoadmapResponse(roadmap);
    }

    @Override
    public void deleteAll() {
        deliveryPersonRepository.deleteAll();
        orderRepository.deleteAll();
        roadmapRepository.deleteAll();
    }

    @Override
    public List<Order> getPendingOrders(boolean isDelivered) {
        return orderRepository.findByIsDelivered(isDelivered);
    }

    @Override
    public ResponseEntity<?> updateRoadmap(Long roadmapId, RoadmapRequest roadmapRequest) {
        if (!roadmapRepository.existsById(roadmapId)){
            return new ResponseEntity<>(new BadRequestException("Not found with id = "+ roadmapId), HttpStatus.BAD_REQUEST);
        }
        Roadmap _roadmap = roadmapRepository.findById(roadmapId).get();

        List<Order> savedOrders = saveOrdersFromRequest(roadmapRequest.getOrderList(), _roadmap);
        //deliveryPerson treatment
        DeliveryPerson deliveryPerson = treatRequestDeliveryPerson(roadmapRequest.getDeliveryPerson());
        deliveryPerson.getRoadmaps().add(_roadmap);
        DeliveryPerson _deliveryPerson = deliveryPersonRepository.save(deliveryPerson);
        //save roadmap
        _roadmap.setOrderList(savedOrders);
        _roadmap.setDeliveryPerson(_deliveryPerson);
        _roadmap.setTotalOrders(savedOrders.size());
        _roadmap.setDelivered(false);
        _roadmap = roadmapRepository.save(_roadmap);

        return new ResponseEntity<>(_roadmap, HttpStatus.OK);
    }

    private List<Order> saveOrdersFromRequest(List<String> orderList, Roadmap roadmap) {
        List<Order> orders = orderRepository.findAll()
                .stream()
                .filter(order -> orderList.contains(order.getOrderNumber()))
                .toList();

        List<Order> _orderList = orders.stream()
                .peek(order -> {
                    order.setRoadmap(roadmap);
                    order.setDelivered(true);
                })
                .toList();
        return orderRepository.saveAll(_orderList);
    }
    private DeliveryPerson treatRequestDeliveryPerson(DeliveryPerson deliveryPerson) {
        if (deliveryPersonRepository.existsById(deliveryPerson.getId())){
            return deliveryPersonRepository.findById(deliveryPerson.getId()).get();
        }else {
            DeliveryPerson _deliveryPerson = new DeliveryPerson();
            List<Roadmap> roadmaps = new ArrayList<>();
            _deliveryPerson = deliveryPerson;
            _deliveryPerson.setRoadmaps(roadmaps);
            _deliveryPerson.setId(deliveryPerson.getId());
            return deliveryPersonRepository.save(_deliveryPerson);
        }
    }
    RoadmapResponse mapToRoadmapResponse(Roadmap roadmap){
        return RoadmapResponse.builder()
                .id(roadmap.getId())
                .date(roadmap.getDate())
                .deliveryPerson(mapToDeliveryPersonResponse(roadmap.getDeliveryPerson()))
                .orderList(roadmap.getOrderList())
                .isDelivered(roadmap.isDelivered())
                .totalOrders(roadmap.getTotalOrders())
                .build();
    }
    DeliveryPersonResponse mapToDeliveryPersonResponse(DeliveryPerson deliveryPerson){
        return DeliveryPersonResponse.builder()
                .name(deliveryPerson.getName())
                .phoneNumber(deliveryPerson.getPhoneNumber())
                .vehiculeNumber(deliveryPerson.getVehiculeNumber())
                .build();
    }

}
