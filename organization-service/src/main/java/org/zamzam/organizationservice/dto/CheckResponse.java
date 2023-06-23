package org.zamzam.organizationservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CheckResponse {
    private String organizationId;
    private boolean isRegistered;
    private boolean isManager;
}
