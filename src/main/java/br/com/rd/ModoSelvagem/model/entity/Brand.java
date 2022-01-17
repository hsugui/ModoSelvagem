package br.com.rd.ModoSelvagem.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "TB_BRAND") @Data
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand_name", nullable = false, length = 150)
    private String brandName;

}
