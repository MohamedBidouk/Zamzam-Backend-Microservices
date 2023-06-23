package org.zamzam.organizationservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.zamzam.organizationservice.model.EOrganization;

@Getter
@Setter
@Builder
public class OrganizationRequest {
    private String id;
    private String name;
    private String taxRegistration;
    private Integer registerNumber;
    private EOrganization type;
}
