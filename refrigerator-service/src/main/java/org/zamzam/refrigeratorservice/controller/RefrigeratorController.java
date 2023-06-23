package org.zamzam.refrigeratorservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zamzam.refrigeratorservice.dto.RefrigeratorRequest;
import org.zamzam.refrigeratorservice.dto.RefrigeratorResponse;
import org.zamzam.refrigeratorservice.dto.UpdateRequest;
import org.zamzam.refrigeratorservice.dto.UpdateResponse;
import org.zamzam.refrigeratorservice.exception.RefrigeratorNotFoundException;
import org.zamzam.refrigeratorservice.model.Refrigerator;
import org.zamzam.refrigeratorservice.repository.RefrigeratorRepository;
import org.zamzam.refrigeratorservice.service.RefrigeratorService;

import java.util.List;

@RestController
@RequestMapping("/api/refrigerators")
@RequiredArgsConstructor
@Slf4j
public class RefrigeratorController {
    private final RefrigeratorRepository refrigeratorRepository;
    private final RefrigeratorService refrigeratorService;


    @GetMapping("/organizations/{organizationId}/refrigerators")
    public ResponseEntity<List<RefrigeratorResponse>> getAllRefrigeratorsByOrganizationId(@PathVariable("organizationId")String organizationId){

        if (!refrigeratorRepository.existsByOrganizationId(organizationId)){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(refrigeratorService.getAllByOrganizationById(organizationId), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Refrigerator> getRefrigeratorById(@PathVariable("id") Long id){

        if (!refrigeratorRepository.existsById(id)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Refrigerator refrigerator = refrigeratorRepository.findById(id).get();
        return new ResponseEntity<>(refrigerator, HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<List<Refrigerator>> getAllRefrigerators(){
        List<Refrigerator> refrigeratorList=refrigeratorRepository.findAll();
        return new ResponseEntity<>(refrigeratorList, HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<RefrigeratorResponse> createRefrigerator(@RequestBody RefrigeratorRequest refrigeratorRequest){
        RefrigeratorResponse response = refrigeratorService.createRefrigerator(refrigeratorRequest);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }
        else {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }
    @PutMapping("/load")
    public ResponseEntity<UpdateResponse> loadRefrigeratorCapacity(@RequestBody UpdateRequest updateRequest){

        if (!refrigeratorRepository.existsById(updateRequest.getRefrigeratorId())){
            throw new RefrigeratorNotFoundException("Refrigerator with id = "+ updateRequest.getRefrigeratorId()+ " not exist");
        }

        Refrigerator refrigerator = refrigeratorRepository.findById(updateRequest.getRefrigeratorId()).get();
        if (!refrigeratorService.canLoadCapacity(refrigerator, updateRequest.getQuantity())){
            return new ResponseEntity<>(new UpdateResponse(refrigerator.getId(), refrigerator.getCurrentCapacity()), HttpStatus.INSUFFICIENT_STORAGE);
        }


        refrigerator.setCurrentCapacity(refrigerator.getCurrentCapacity() + updateRequest.getQuantity());
        refrigeratorRepository.save(refrigerator);

        return new ResponseEntity<>(new UpdateResponse(refrigerator.getId(), refrigerator.getCurrentCapacity()), HttpStatus.OK);
    }
    @GetMapping("/canUnload/{refrigeratorId}/{quantity}")
    public ResponseEntity<Boolean> canRefrigeratorHandleRequest(@PathVariable("refrigeratorId") Long refrigeratorId,
                                                                @PathVariable("quantity") int quantity){
        //check if refrigerator exist
        if (!refrigeratorRepository.existsById(refrigeratorId)){
            throw new RefrigeratorNotFoundException("Refrigerator with id = "+ refrigeratorId+ " not exist");
        }
        //check if refrigerator can handle request
        Refrigerator refrigerator = refrigeratorRepository.findById(refrigeratorId).get();
        if (!refrigeratorService.canUnloadCapacity(refrigerator, quantity)){
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
    @PutMapping("/unload")
    public boolean unloadRefrigeratorCapacity(@RequestBody UpdateRequest updateRequest){
        //check if refrigerator exist
        if (!refrigeratorRepository.existsById(updateRequest.getRefrigeratorId())){
            throw new RefrigeratorNotFoundException("Refrigerator with id = "+ updateRequest.getRefrigeratorId()+ " not exist");
        }
        Refrigerator refrigerator = refrigeratorRepository.findById(updateRequest.getRefrigeratorId()).get();
        if (!refrigeratorService.canUnloadCapacity(refrigerator, updateRequest.getQuantity())){
            return false;
        }

        refrigerator.setCurrentCapacity(refrigerator.getCurrentCapacity() - updateRequest.getQuantity());
        refrigeratorRepository.save(refrigerator);

        return true;
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteRefrigeratorById(@PathVariable("id")Long id){
        refrigeratorRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("/organizations/{organizationId}/refrigerators")
    public ResponseEntity<HttpStatus> deleteByOrganizationId(@PathVariable("organizationId") String organizationId){
        refrigeratorRepository.deleteAllByOrganizationId(organizationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Exception handler
    @ExceptionHandler(RefrigeratorNotFoundException.class)
    public ResponseEntity<String> handleRefrigeratorNotFoundException(RefrigeratorNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
