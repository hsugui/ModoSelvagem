package br.com.rd.ModoSelvagem.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class CustomerCheckoutDTO {

    private Long id;
    private String name;
    private String surname;
    private String cpf;
    private String email;
    private List<AddressDTO> addresses;

}
