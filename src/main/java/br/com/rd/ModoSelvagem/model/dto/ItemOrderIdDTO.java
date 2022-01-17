package br.com.rd.ModoSelvagem.model.dto;

import br.com.rd.ModoSelvagem.model.entity.Order;
import lombok.Data;

@Data
public class ItemOrderIdDTO {

    private Long itemId;
    private Order orderId;

}
