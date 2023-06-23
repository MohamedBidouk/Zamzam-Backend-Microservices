package org.zamzam.organizationservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.zamzam.organizationservice.model.Organization;

@Getter
@Setter
@Builder
public class ManagerResquest {
    private String managerId;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Organization organization;
}
