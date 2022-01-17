package br.com.rd.ModoSelvagem.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "TB_PRODUCT_IMAGE") @Data
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private String path;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
