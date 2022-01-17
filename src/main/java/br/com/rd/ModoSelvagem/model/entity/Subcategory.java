package br.com.rd.ModoSelvagem.model.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "TB_SUBCATEGORY")
@Data
public class Subcategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subcategory_name", nullable = false, length = 150)
    private String subcategoryName;

    @ToString.Exclude
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

}
