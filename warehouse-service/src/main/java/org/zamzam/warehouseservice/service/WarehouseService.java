package org.zamzam.warehouseservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zamzam.warehouseservice.dto.CapacityResponse;
import org.zamzam.warehouseservice.dto.UpdateRequest;
import org.zamzam.warehouseservice.dto.WarehouseRequest;
import org.zamzam.warehouseservice.dto.WarehouseResponse;
import org.zamzam.warehouseservice.exception.CapacityExceededException;
import org.zamzam.warehouseservice.exception.ResourceNotFoundException;
import org.zamzam.warehouseservice.model.Warehouse;
import org.zamzam.warehouseservice.repository.WarehouseRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;

    public boolean canAddCapacity(Warehouse warehouse, Integer quantity){
        return warehouse.getMaxCapacity() - warehouse.getCurrentCapacity() > quantity;
    }

    public List<CapacityResponse> getWarehousesCapacityDataByOrganizationId(String organizationId) {
        return warehouseRepository.findByOrganizationId(organizationId)
                .stream()
                .map(this::mapToCapacityResponse)
                .collect(Collectors.toList());
    }

    private CapacityResponse mapToCapacityResponse(Warehouse warehouse) {
        return CapacityResponse.builder()
                .warehouseId(warehouse.getId())
                .currentCapacity(warehouse.getCurrentCapacity())
                .maxCapacity(warehouse.getMaxCapacity())
                .build();
    }

    public boolean existsByOrganizationId(String organizationId) {
        return warehouseRepository.existsByOrganizationId(organizationId);
    }

    public List<WarehouseResponse> findAll() {
        return warehouseRepository.findAll()
                .stream()
                .map(this::mapToWarehouseResponse)
                .collect(Collectors.toList());
    }


    public List<WarehouseResponse> findByOrganizationId(String organizationId) {
        return warehouseRepository.findByOrganizationId(organizationId)
                .stream()
                .map(this::mapToWarehouseResponse)
                .collect(Collectors.toList());
    }

    private WarehouseResponse mapToWarehouseResponse(Warehouse warehouse) {
        return WarehouseResponse.builder()
                .id(warehouse.getId())
                .currentCapacity(warehouse.getCurrentCapacity())
                .maxCapacity(warehouse.getMaxCapacity())
                .streetAddress(warehouse.getStreetAddress())
                .state(warehouse.getState())
                .city(warehouse.getCity())
                .build();
    }

    public boolean existsById(Long id) {
        return warehouseRepository.existsById(id);
    }

    public Optional<Warehouse> findById(Long id) {
        return warehouseRepository.findById(id);
    }
    public Warehouse createWarehouse(WarehouseRequest warehouseRequest){
        Warehouse warehouse = new Warehouse();

        warehouse.setOrganizationId(warehouseRequest.getOrganizationId());
        warehouse.setCurrentCapacity(warehouseRequest.getCurrentCapacity());
        warehouse.setMaxCapacity(warehouseRequest.getMaxCapacity());
        warehouse.setCity(warehouseRequest.getCity());
        warehouse.setState(warehouseRequest.getState());
        warehouse.setStreetAddress(warehouseRequest.getStreetAddress());
        return warehouseRepository.save(warehouse);
    }

    public void deleteById(Long warehouseId) {
        this.warehouseRepository.deleteById(warehouseId);
    }

    public WarehouseResponse updateWarehouse(Long warehouseId, WarehouseRequest warehouseRequest) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElseThrow(()->
            new ResourceNotFoundException("Warehouse with ID " + warehouseId + " not found")
        );

        warehouse.setCity(warehouseRequest.getCity());
        warehouse.setState(warehouseRequest.getState());
        warehouse.setStreetAddress(warehouseRequest.getStreetAddress());
        warehouse.setCurrentCapacity(warehouseRequest.getCurrentCapacity());
        warehouse.setMaxCapacity(warehouseRequest.getMaxCapacity());

        return mapToWarehouseResponse(warehouseRepository.save(warehouse));
    }

    public WarehouseResponse updateWarehouseCapacity(UpdateRequest updateRequest) {
        Warehouse warehouse = warehouseRepository.findById(updateRequest.getWarehouseId())
                .orElseThrow(()-> new ResourceNotFoundException("Warehouse with ID " + updateRequest.getWarehouseId() + " not found"));
        if (!canAddCapacity(warehouse, updateRequest.getQuantity())){
            throw new CapacityExceededException("Adding the specified capacity would exceed the maximum capacity of the warehouse");
        }
        warehouse.setCurrentCapacity(warehouse.getCurrentCapacity() + updateRequest.getQuantity());

        return mapToWarehouseResponse(warehouseRepository.save(warehouse));

    }
}
