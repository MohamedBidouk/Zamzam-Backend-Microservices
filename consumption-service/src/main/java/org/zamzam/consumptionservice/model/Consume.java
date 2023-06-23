package org.zamzam.consumptionservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "consumption")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Consume {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @CreationTimestamp
    private Date consumptionTime;
    private String consumerId;
    private Long refrigeratorId;
    private Integer quantity;


}
