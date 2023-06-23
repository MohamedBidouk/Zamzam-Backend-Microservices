package org.zamzam.deliveryservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "deliveryPerson")
public class DeliveryPerson {
    @Id
    private Long id;
    private String name;
    private String phoneNumber;
    private String vehiculeNumber;
    @OneToMany
    private List<Roadmap> roadmaps;
}
