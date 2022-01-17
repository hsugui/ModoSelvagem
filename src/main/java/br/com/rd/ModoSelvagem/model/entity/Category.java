package br.com.rd.ModoSelvagem.model.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "TB_CATEGORY") @Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name", nullable = false, length = 150)
    private String categoryName;

    @ToString.Exclude
    @OneToMany(mappedBy = "category", orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Subcategory> subcategories = new ArrayList<>();

}
