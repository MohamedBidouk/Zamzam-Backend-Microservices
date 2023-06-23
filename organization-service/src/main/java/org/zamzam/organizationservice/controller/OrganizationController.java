package org.zamzam.organizationservice.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zamzam.organizationservice.dto.CheckResponse;
import org.zamzam.organizationservice.dto.ManagerResponse;
import org.zamzam.organizationservice.dto.OrganizationRequest;
import org.zamzam.organizationservice.dto.OrganizationResponse;
import org.zamzam.organizationservice.model.Manager;
import org.zamzam.organizationservice.service.OrganizationService;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;
    @GetMapping()
    public ResponseEntity<List<OrganizationResponse>> getAllOrganizations(){
        return new ResponseEntity<>(organizationService.getAllOrganization(), HttpStatus.OK);
    }
    @GetMapping("/wholesaler")
    public ResponseEntity<List<OrganizationResponse>> getWholesalerOrganizations(){
        return new ResponseEntity<>(organizationService.getAllWholesalerOrganization(), HttpStatus.OK);
    }
    /*
    @GetMapping("/{organizationId}")
    public ResponseEntity<OrganizationResponse> getOrganizationById(@PathVariable("organizationId")Long organizationId){
        OrganizationResponse organizationResponse = organizationService.getOrganizationById(organizationId);
        if (organizationResponse == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(organizationResponse, HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<?> createOrganization(@RequestBody OrganizationRequest organizationRequest){
        OrganizationResponse organizationResponse;
        try {
            organizationResponse= organizationService.createOrganization(organizationRequest);
            return new  ResponseEntity<>(organizationResponse, HttpStatus.CREATED);
        }catch (DataIntegrityViolationException exception){
            return new ResponseEntity<>("Name or Email or Tax Registration or Register Number already exists", HttpStatus.CONFLICT);
        }
    }
    @PutMapping("/{organizationId}")
    public ResponseEntity<OrganizationResponse> updateOrganizationById(@PathVariable("organizationId")Long organizationId,
                                                                       @RequestBody OrganizationRequest organizationRequest){
        return new ResponseEntity<>(organizationService.updateOrganization(organizationId, organizationRequest), HttpStatus.OK);
    }
    @PutMapping("/confirmEmail/{token}")
    public ResponseEntity<String> validateToken(@PathVariable("token")String token){
        return new ResponseEntity<>(organizationService.validateToken(token), HttpStatus.OK);
    }
    @DeleteMapping("/{organizationId}")
    public  ResponseEntity<HttpStatus> deleteById(@PathVariable("organizationId") Long organizationId){
        organizationService.deleteById(organizationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
*/
    @GetMapping("/check/{organizationId}")
    public ResponseEntity<CheckResponse> checkOrganization(@PathVariable("organizationId")String loggedInId){
        return new ResponseEntity<>(organizationService.checkOrganization(loggedInId), HttpStatus.OK);
    }
    @GetMapping("/{organizationId}/managers")
    public ResponseEntity<ManagerResponse> managersPerOrganization(@PathVariable("organizationId")String organizationId){
        List<ManagerResponse> managers = organizationService.getManagersByOrganizationId(organizationId);
        if (managers.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(managers, HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<OrganizationResponse> crateOrganization(@Valid @RequestBody OrganizationRequest organizationRequest){
        OrganizationResponse organizationResponse = organizationService.createOrganization(organizationRequest);
        return new ResponseEntity<>(organizationResponse, HttpStatus.CREATED);
    }
    @PutMapping("/add-manager/{organizationId}")
    public ResponseEntity<ManagerResponse> addManagerToOrganization(@PathVariable("organizationId")String organizationId,
                                                      @RequestBody Manager manager){
        ManagerResponse response = organizationService.saveManager(organizationId, manager);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
