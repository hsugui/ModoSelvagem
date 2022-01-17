package br.com.rd.ModoSelvagem.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB_INVOICE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(length = 100)
    private String customerName;

    @Column(length = 100)
    private String customerSurname;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(length = 50, nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String companyAdress;

    @Column(length = 16, nullable = false)
    private String companyPhone;

    @Column(nullable = false, length = 50)
    private String companyEmail;

    @Column(nullable = false, length = 16)
    private String stateRegistration; //Inscrição estadual

    @Column(length = 19, nullable = false)
    private String cnpj;

    @Column(nullable = false, length = 30)
    private String invoiceNumber;

    @Column(nullable = false, length = 3)
    private String invoiceSequence;

    @Column(nullable = false, length = 60)
    private String accessKey;

    @Column(nullable = false)
    private LocalDateTime issueDate;

    @Column(nullable = false, precision = 12, scale = 4)
    private Double totalPis;

    @Column(nullable = false, precision = 12, scale = 4)
    private Double totalCofins;

    @Column(nullable = false, precision = 12, scale = 4)
    private Double totalIcms;

    @Column(nullable = false, precision = 12, scale = 4)
    private Double totalIpi;

    @Column(nullable = false, precision = 12, scale = 4)
    private Double shippingValue;

    @Column(nullable = false, precision = 12, scale = 4)
    private Double totalProductsValue;

    @Column(nullable = false, precision = 12, scale = 4)
    private Double totalPurchaseValue;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "TB_INVOICE_ITEM",
            joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> items = new ArrayList<>();

}
