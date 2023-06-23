package org.zamzam.refrigeratorservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zamzam.refrigeratorservice.dto.RefrigeratorRequest;
import org.zamzam.refrigeratorservice.dto.RefrigeratorResponse;
import org.zamzam.refrigeratorservice.model.Refrigerator;
import org.zamzam.refrigeratorservice.repository.RefrigeratorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RefrigeratorService {
    private final RefrigeratorRepository refrigeratorRepository;

    public boolean canLoadCapacity(Refrigerator refrigerator, Integer quantity){
        return refrigerator.getMaxCapacity() - refrigerator.getCurrentCapacity() > quantity;
    }
    public boolean canUnloadCapacity(Refrigerator refrigerator, Integer quantity){

        return refrigerator.getCurrentCapacity() > quantity;
    }
    public RefrigeratorResponse createRefrigerator(RefrigeratorRequest refrigeratorRequest){
        if (refrigeratorRepository.existsBySerialNumber(refrigeratorRequest.getSerialNumber())){
            return null;
        }
        Refrigerator refrigerator = new Refrigerator();
        refrigerator.setOrganizationId(refrigeratorRequest.getOrganizationId());
        refrigerator.setCurrentCapacity(refrigeratorRequest.getCurrentCapacity());
        refrigerator.setSerialNumber(refrigeratorRequest.getSerialNumber());
        refrigerator.setMaxCapacity(refrigeratorRequest.getMaxCapacity());
        refrigerator.setAddress(refrigeratorRequest.getAddress());
        refrigerator = refrigeratorRepository.save(refrigerator);
        return mapToRefrigeratorResponse(refrigerator);
    }
    public List<RefrigeratorResponse> getAllByOrganizationById(String organizationId){
        return refrigeratorRepository.findByOrganizationId(organizationId).stream()
                .map(this::mapToRefrigeratorResponse)
                .collect(Collectors.toList());
    }

    private RefrigeratorResponse mapToRefrigeratorResponse(Refrigerator refrigerator) {
        return RefrigeratorResponse.builder()
                .id(refrigerator.getId())
                .serialNumber(refrigerator.getSerialNumber())
                .address(refrigerator.getAddress())
                .currentCapacity(refrigerator.getCurrentCapacity())
                .maxCapacity(refrigerator.getMaxCapacity())
                .build();
    }
}
