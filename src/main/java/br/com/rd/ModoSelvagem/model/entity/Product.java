package br.com.rd.ModoSelvagem.model.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "TB_PRODUCT") @Data
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false, length = 150, unique = true)
    private String name;

    @Column(nullable = false, length = 1000)
    private String shortDescription;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String fullDescription;

    @Column(nullable = false, length = 15)
    private String weight;

    @Column(nullable = false, length = 50)
    private String material;

    @Column(nullable = false, length = 50)
    private String color;

    @Column(nullable = false, length = 50)
    private String dimensions;

    @Column(nullable = false, length = 50)
    private String warranty;

    @Column(nullable = false)
    private LocalDateTime updateDate;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;

    @OneToMany(mappedBy = "product", orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<ProductImage> images = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "product", orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<SalePrice> prices = new ArrayList<>();

    @ToString.Exclude
    @OneToOne(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @PrimaryKeyJoinColumn
    private Storage storage;



}
