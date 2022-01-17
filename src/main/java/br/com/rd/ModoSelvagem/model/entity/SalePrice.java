package br.com.rd.ModoSelvagem.model.entity;

import br.com.rd.ModoSelvagem.model.embeddable.SalePriceId;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "TB_SALE_PRICE")
@Data
public class SalePrice {

    @EmbeddedId
    private SalePriceId salePriceId;

    @Column(nullable = false, precision=12, scale=2)
    private Double price;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "product_id", updatable = false, insertable = false)
    private Product product;

}
