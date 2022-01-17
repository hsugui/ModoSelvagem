package br.com.rd.ModoSelvagem.model.dto;

import br.com.rd.ModoSelvagem.model.entity.Address;
import br.com.rd.ModoSelvagem.model.entity.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy – HH:mm:ss")
    private LocalDateTime orderDate;

    @NotNull
    private Double totalPrice;

    private Integer installmentsNumber;

    private Double installmentsValue;

    @NotEmpty
    private String deliveryType;

    @NotNull
    private Double shippingValue;

    @NotNull
    private Integer deliveryTime;

    @NotNull
    private OrderStatusDTO status;

    @NotNull
    private CustomerCheckoutDTO customer;

    @NotNull
    private PaymentMethodDTO paymentMethod;

    private AddressDTO address;

    @NotNull
    private List<ItemOrderDTO> items = new ArrayList<>();

}
