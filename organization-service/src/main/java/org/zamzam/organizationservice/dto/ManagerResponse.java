package org.zamzam.organizationservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ManagerResponse {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
