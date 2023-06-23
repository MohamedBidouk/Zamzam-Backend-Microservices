package org.zamzam.organizationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zamzam.organizationservice.dto.CheckResponse;
import org.zamzam.organizationservice.dto.ManagerResponse;
import org.zamzam.organizationservice.dto.OrganizationRequest;
import org.zamzam.organizationservice.dto.OrganizationResponse;
import org.zamzam.organizationservice.model.EOrganization;
import org.zamzam.organizationservice.model.Manager;
import org.zamzam.organizationservice.model.Organization;
import org.zamzam.organizationservice.repositories.ManagerRepository;
import org.zamzam.organizationservice.repositories.OrganizationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final ManagerRepository managerRepository;

    public CheckResponse checkOrganization(String loggedInId) {
        log.info("loggedIn",loggedInId);
        if (organizationRepository.existsById(loggedInId)){
            return CheckResponse.builder()
                    .organizationId(loggedInId)
                    .isRegistered(true)
                    .isManager(false)
                    .build();
        }
        if (managerRepository.existsById(loggedInId)) {
            Manager manager = managerRepository.findById(loggedInId).get();
            log.info(manager.getOrganization().getId());
            return CheckResponse.builder()
                    .organizationId(manager.getOrganization().getId())
                    .isRegistered(false)
                    .isManager(true)
                    .build();

        }else {
            return CheckResponse.builder()
                    .organizationId(null)
                    .isRegistered(false)
                    .isManager(false)
                    .build();
        }

    }

    public OrganizationResponse createOrganization(OrganizationRequest organizationRequest) {
        Organization organization = new Organization();
        List<String> managersIds = new ArrayList<>();
        organization.setId(organizationRequest.getId());
        organization.setName(organizationRequest.getName());
        organization.setType(organizationRequest.getType());
        organization.setTaxRegistration(organizationRequest.getTaxRegistration());
        organization.setRegisterNumber(organizationRequest.getRegisterNumber());
        organization.setIdentities(managersIds);

        return mapToOrganizationResponse(organizationRepository.save(organization));
    }
    public List<OrganizationResponse> getAllOrganization(){
        return organizationRepository.findAll().stream()
                .map(this::mapToOrganizationResponse)
                .collect(Collectors.toList());
    }
    public List<OrganizationResponse> getAllWholesalerOrganization(){
        return organizationRepository.findAll().stream()
                .filter(organization -> organization.getType()== EOrganization.Wholesaler)
                .map(this::mapToOrganizationResponse)
                .collect(Collectors.toList());
    }

    public ManagerResponse saveManager(String organizationId, Manager managerResquest) {
        Manager manager = organizationRepository.findById(organizationId).map(organization ->{
            managerResquest.setOrganization(organization);
            Manager _manager = managerRepository.save(managerResquest);
            organization.getManagers().add(_manager);
            organizationRepository.save(organization);
            return _manager;
        }).orElseThrow(()-> new RuntimeException("not found"));
        return ManagerResponse.builder()
                .username(manager.getUsername())
                .firstname(manager.getFirstname())
                .lastname(manager.getLastname())
                .email(manager.getEmail())
                .password(manager.getPassword())
                .build();
    }

    public List<ManagerResponse> getManagersByOrganizationId(String organizationId) {
        List<Manager> managers = managerRepository.findByOrganizationId(organizationId);
        return managers.stream()
                .map(this::mapToManagerResponse)
                .collect(Collectors.toList());
    }
    //Mappers
    private OrganizationResponse mapToOrganizationResponse(Organization organization) {
        return OrganizationResponse.builder()
                .id(organization.getId())
                .identities(organization.getIdentities())
                .name(organization.getName())
                .type(organization.getType())
                .registerNumber(organization.getRegisterNumber())
                .taxRegistration(organization.getTaxRegistration())
                .build();
    }
    private ManagerResponse mapToManagerResponse(Manager manager) {
        return ManagerResponse.builder()
                .username(manager.getUsername())
                .firstname(manager.getFirstname())
                .lastname(manager.getLastname())
                .email(manager.getEmail())
                .password(manager.getPassword())
                .build();
    }
    /*private final TokenRepository tokenRepository;
    private final JavaMailSender javaMailSender;
    public ValidationToken createToken(Organization organization){
        ValidationToken validationToken = new ValidationToken();
        validationToken.setOrganization(organization);
        return tokenRepository.save(validationToken);
    }
    int compareTime(Date creationDate){
        Date now = new Date();
        long timeDifference = now.getTime() - creationDate.getTime();
        return (int) TimeUnit.MILLISECONDS.toHours(timeDifference);
    }
    public String validateToken(String token){
        if (!tokenRepository.existsById(token)){
            return "Not exist";
        }
        ValidationToken validationToken = tokenRepository.findById(token).get();

        if (compareTime(validationToken.getCreationDate()) >= 24){
            sendConfirmationEmail(validationToken.getOrganization().getOwnerEmail(), createToken(validationToken.getOrganization()).getVerificationToken());
            return "token expired";
        }else {
            Organization organization = organizationRepository.findById(validationToken.getOrganization().getId()).get();
            organization.setActive(true);
            organizationRepository.save(organization);
            tokenRepository.deleteById(token);
            return "Account verified";
        }
    }

    public OrganizationResponse createOrganization(OrganizationRequest organizationRequest){
        Organization organization = new Organization();


        return getOrganizationResponse(organizationRequest, organization, organizationRequest.getType(), false);
    }
    public OrganizationResponse updateOrganization(Long organizationId, OrganizationRequest organizationRequest){
        if (!organizationRepository.existsById(organizationId)){
            return null;
        }
        Organization organization = organizationRepository.findById(organizationId).get();
        return getOrganizationResponse(organizationRequest, organization, organization.getType(), organization.isActive());
    }

    private OrganizationResponse getOrganizationResponse(OrganizationRequest organizationRequest, Organization organization, EOrganization type, boolean isActive) {
        organization.setName(organizationRequest.getName());
        organization.setType(type);
        organization.setActive(isActive);
        organization.setOwnerEmail(organizationRequest.getOwnerEmail());
        organization.setRegisterNumber(organizationRequest.getRegisterNumber());
        organization.setTaxRegistration(organizationRequest.getTaxRegistration());
        Organization _organization = organizationRepository.save(organization);
        sendConfirmationEmail("mohamedbidouk1998@gmail.com", createToken(_organization).getVerificationToken());
        return mapToOrganizationResponse(_organization);
    }

    public OrganizationResponse getOrganizationById(Long organizationId){
        if (!organizationRepository.existsById(organizationId)){
            return null;
        }
        return mapToOrganizationResponse(organizationRepository.findById(organizationId).get());
    }
    public List<OrganizationResponse> getAllOrganization(){
        return organizationRepository.findAll().stream()
                .map(this::mapToOrganizationResponse)
                .collect(Collectors.toList());
    }

    private OrganizationResponse mapToOrganizationResponse(Organization organization) {
        return OrganizationResponse.builder()
                .name(organization.getName())
                .ownerEmail(organization.getOwnerEmail())
                .managersEmails(organization.getManagersEmails())
                .type(organization.getType())
                .registerNumber(organization.getRegisterNumber())
                .taxRegistration(organization.getTaxRegistration())
                .isActive(organization.isActive())
                .build();
    }

    public void deleteById(Long organizationId){
        organizationRepository.deleteById(organizationId);
    }
    public void sendConfirmationEmail(String toEmail, String verificationToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Please confirm your email address");
        message.setText("Please click the link below to confirm your email address:\n\n"
                + "http://localhost:8080/api/organizations/confirmEmail/" + verificationToken);
        javaMailSender.send(message);
    }*/
}
