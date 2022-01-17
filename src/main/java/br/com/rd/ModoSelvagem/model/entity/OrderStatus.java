package br.com.rd.ModoSelvagem.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "tb_order_status")
@Data
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private String description;

}
