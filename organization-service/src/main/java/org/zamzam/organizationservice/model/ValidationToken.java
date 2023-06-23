package org.zamzam.organizationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String verificationToken;
    @CreationTimestamp
    private Date creationDate;
    @ManyToOne()
    private Organization organization;
}
