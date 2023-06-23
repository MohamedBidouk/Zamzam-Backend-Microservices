package org.zamzam.refrigeratorservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "refrigerators")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Refrigerator {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String organizationId;
    private String serialNumber;
    private Integer currentCapacity;
    private Integer maxCapacity;
    private String address;
}
