package br.com.rd.ModoSelvagem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemOrderDTO {

    private Long productId;
    private Long orderId;
    private Integer quantity;
    private Double discount;
    private Double grossValue;
    private Double netValue;
    private ProductImageDTO productImage;
    private String productName;
}
