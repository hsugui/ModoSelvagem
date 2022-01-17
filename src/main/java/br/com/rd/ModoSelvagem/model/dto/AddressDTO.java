package br.com.rd.ModoSelvagem.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class AddressDTO {

    private Long id;

    @NotEmpty(message = "This field must not be empty.")
    @Size(min = 8, max = 8)
    private String cep;

    @NotEmpty(message = "This field must not be empty.")
    @Size(max = 100)
    private String street;

    @NotNull(message = "This field must not be empty.")
    @Size(min = 1)
    @Min(1)
    private Integer number;

    @Size(max = 50)
    private String complement;

    @NotEmpty(message = "This field must not be empty.")
    @Size(max = 100)
    private String district;

    @NotEmpty(message = "This field must not be empty.")
    @Size(max = 100)
    private String city;

    @NotEmpty(message = "This field must not be empty.")
    @Size(min = 2, max = 2)
    private String uf;
}


