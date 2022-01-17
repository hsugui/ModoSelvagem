package br.com.rd.ModoSelvagem.model.entity;

import lombok.Data;
import javax.persistence.*;

@Entity(name = "tb_payment_method")
@Data
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    private String paymentMethodName;

}
