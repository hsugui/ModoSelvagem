package br.com.rd.ModoSelvagem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAddressesDTO {
    private Long customerId;
    private String customerName;
    private List<AddressDTO> addresses;
}

