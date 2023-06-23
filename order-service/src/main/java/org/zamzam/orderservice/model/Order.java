package org.zamzam.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String orderNumber;
    private String senderOrganizationId;
    private String receiverOrganizationId;
    private Long receiverDepotId;
    private Integer quantity;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @CreationTimestamp
    private Date createdAt;
}
