package org.zamzam.organizationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Organization {
    @Id
    private String id;
    @ElementCollection
    private List<String> identities;
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String taxRegistration;
    private Integer registerNumber;
    @Enumerated(EnumType.STRING)
    private EOrganization type;
    @OneToMany
    private List<Manager> managers;

}
