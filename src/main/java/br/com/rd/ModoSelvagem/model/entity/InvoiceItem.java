package br.com.rd.ModoSelvagem.model.entity;

import br.com.rd.ModoSelvagem.model.embeddable.InvoiceItemId;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Entity(name = "TB_INVOICE_ITEM")
@Data
@IdClass(InvoiceItemId.class)
public class InvoiceItem implements Serializable {

    @Id
    @Column(name = "invoice_id")
    private Long invoiceId;
    @Id
    @Column(name = "product_id")
    private Long productId;
    @Column(nullable = false)
    private Double pcIcms;
    @Column(nullable = false)
    private Double icmsAmount;
    @Column(nullable = false)
    private Double pcIpi;
    @Column(nullable = false)
    private Double ipiAmount;
    @Column(nullable = false)
    private Double pcCofins;
    @Column(nullable = false)
    private Double cofinsAmount;
    @Column(nullable = false)
    private Double pcPis;
    @Column(nullable = false)
    private Double pisAmount;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private Double discount;
    @Column(nullable = false, precision=12, scale=4)
    private Double grossValue;
    @Column(nullable = false, precision=12, scale=4)
    private Double netValue;
    @Column(nullable = false)
    private String productName;
}
