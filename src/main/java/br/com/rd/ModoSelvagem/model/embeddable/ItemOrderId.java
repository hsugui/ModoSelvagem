package br.com.rd.ModoSelvagem.model.embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@AllArgsConstructor //com argumentos
@NoArgsConstructor //sem argumentos

public class ItemOrderId implements Serializable {

    private Long productId;
    private Long orderId;
}
