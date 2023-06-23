package org.zamzam.warehouseservice.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zamzam.warehouseservice.dto.CapacityResponse;
import org.zamzam.warehouseservice.dto.UpdateRequest;
import org.zamzam.warehouseservice.dto.WarehouseRequest;
import org.zamzam.warehouseservice.dto.WarehouseResponse;
import org.zamzam.warehouseservice.exception.ResourceNotFoundException;
import org.zamzam.warehouseservice.model.Warehouse;
import org.zamzam.warehouseservice.repository.WarehouseRepository;
import org.zamzam.warehouseservice.service.WarehouseService;

import java.util.List;

@RestController
@RequestMapping("/api/warehouse")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseService warehouseService;

    @GetMapping
    public ResponseEntity<List<WarehouseResponse>> getAllWarehouses(){
        List<WarehouseResponse> warehouseList ;

        warehouseList=warehouseService.findAll();
        if (warehouseList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(warehouseList, HttpStatus.OK);
    }

    @GetMapping("/organizations/{organizationId}")
    public ResponseEntity<?> getAllWarehousesByOrganizationId(@PathVariable("organizationId")String organizationId){

        if (!warehouseService.existsByOrganizationId(organizationId)){
            return new ResponseEntity<>(new ResourceNotFoundException("Not found organization with id =" + organizationId),
                    HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(warehouseService.findByOrganizationId(organizationId), HttpStatus.OK);
    }
    @GetMapping("/loadRequest")
    @ResponseStatus(HttpStatus.OK)
    public boolean canLoad(@RequestParam Long id,
                           @RequestParam Integer quantity){
        Warehouse warehouse = warehouseRepository.findById(id).get();

        return warehouse.getMaxCapacity() - warehouse.getCurrentCapacity() > quantity;
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getWarehouseById(@PathVariable("id") Long id){

        if (!warehouseService.existsById(id)){
            return new ResponseEntity<>(new ResourceNotFoundException("Not found warehouse with id = "+ id),
                    HttpStatus.NO_CONTENT);
        }
        Warehouse warehouse = warehouseService.findById(id).get();
        return new ResponseEntity<>(warehouse, HttpStatus.OK);
    }
    @GetMapping("/organizations/{organizationId}/warehouses/capacity")
    public ResponseEntity<?> getCapacityDataForOrgWarehouses(@PathVariable("organizationId")String organizationId){
        List<CapacityResponse> capacityResponseList;
        if (!warehouseService.existsByOrganizationId(organizationId)){
            return new ResponseEntity<>(new ResourceNotFoundException("Not found organization with id =" + organizationId),
                    HttpStatus.NO_CONTENT);
        }

       capacityResponseList = warehouseService.getWarehousesCapacityDataByOrganizationId(organizationId);
        if (!capacityResponseList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(capacityResponseList, HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<Warehouse> createWarehouse(@RequestBody WarehouseRequest warehouseRequest){
        return new ResponseEntity<>(warehouseService.createWarehouse(warehouseRequest) ,HttpStatus.CREATED);
    }
    @PutMapping("/load")
    public ResponseEntity<?> updateWarehouseCapacity(@RequestBody UpdateRequest updateRequest){

        return new ResponseEntity<>(warehouseService.updateWarehouseCapacity(updateRequest), HttpStatus.OK);
    }
    @PutMapping("/modify/{warehouseId}")
    public ResponseEntity<WarehouseResponse> updateWarehouse(@PathVariable("warehouseId")Long warehouseId,
                                         @RequestBody WarehouseRequest warehouseRequest){
        WarehouseResponse updatedWarehouse = warehouseService.updateWarehouse(warehouseId,warehouseRequest);
        return new ResponseEntity<>(updatedWarehouse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteWarehouseById(@PathVariable("id")Long id){
        warehouseService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("/organizations/{organizationId}")
    public ResponseEntity<HttpStatus> deleteByOrganizationId(@PathVariable("organizationId") String organizationId){
        warehouseRepository.deleteAllByOrganizationId(organizationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
