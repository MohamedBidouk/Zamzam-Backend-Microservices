package org.zamzam.organizationservice.dto;

import lombok.Builder;
import lombok.Data;
import org.zamzam.organizationservice.model.EOrganization;

import java.util.List;
@Data
@Builder
public class OrganizationResponse {
    private String id;
    private String name;
    private List<String> identities;
    private String taxRegistration;
    private Integer registerNumber;
    private EOrganization type;
}
