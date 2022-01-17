package br.com.rd.ModoSelvagem.model.embeddable;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Data
public class SalePriceId implements Serializable {

    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false)
    private LocalDate validity;

}
