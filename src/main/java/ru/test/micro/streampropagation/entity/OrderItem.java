package ru.test.micro.streampropagation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class OrderItem {

    @Id
    @GeneratedValue
    private Long id;

    private String product;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
