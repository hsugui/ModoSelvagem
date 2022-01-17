package br.com.rd.ModoSelvagem.model.dto;

import br.com.rd.ModoSelvagem.model.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {

    private Long id;
    private Long customerId;
    private String customerName;
    private String customerSurname;
    private Long orderId;
    private String companyName;
    private String companyAdress;
    private String companyPhone;
    private String companyEmail;
    private String stateRegistration; //Inscrição estadual
    private String cnpj;
    private String invoiceNumber;
    private String invoiceSequence;
    private String accessKey;
    private LocalDateTime issueDate;
    private Double totalPis;
    private Double totalCofins;
    private Double totalIcms;
    private Double totalIpi;
    private Double shippingValue;
    private Double totalProductsValue;
    private Double totalPurchaseValue;
    private List<InvoiceItemDTO> items = new ArrayList<>();
}
