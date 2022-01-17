package br.com.rd.ModoSelvagem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceItemDTO {

    private Long invoiceId;
    private Long productId;
    private String productName;
    private Double pcIcms;
    private Double icmsAmount;
    private Double pcIpi;
    private Double ipiAmount;
    private Double pcCofins;
    private Double cofinsAmount;
    private Double pcPis;
    private Double pisAmount;
    private Integer quantity;
    private Double discount;
    private Double grossValue;
    private Double netValue;
}
