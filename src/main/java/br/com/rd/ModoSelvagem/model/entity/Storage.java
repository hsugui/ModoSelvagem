package br.com.rd.ModoSelvagem.model.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "TB_STORAGE")
@Data
public class Storage {

    @Id
    @Column(name = "product_id")
    private Long id;

    @ToString.Exclude
    @OneToOne
    @MapsId
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

}
