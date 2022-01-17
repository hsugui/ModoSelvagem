package br.com.rd.ModoSelvagem.model.entity;

import br.com.rd.ModoSelvagem.model.embeddable.ItemOrderId;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "TB_ITEM_ORDER")
@Data
@IdClass(ItemOrderId.class)
public class ItemOrder implements Serializable {

    @Id
    @Column(name = "product_id")
    private Long productId;

    @Id
    @Column(name = "order_id")
    private Long orderId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision=12, scale=4)
    private Double discount;

    @Column(nullable = false, precision=12, scale=4)
    private Double grossValue;

    @Column(nullable = false, precision=12, scale=4)
    private Double netValue;

}
