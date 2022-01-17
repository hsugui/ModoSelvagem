package br.com.rd.ModoSelvagem.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class GenderDTO {

    private Long id;

    @NotBlank(message = "This field must not be empty.")
    @Size(max = 50)
    private String description;

}

