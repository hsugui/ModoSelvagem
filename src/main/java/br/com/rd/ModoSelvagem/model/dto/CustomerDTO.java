package br.com.rd.ModoSelvagem.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private Long id;

    @NotEmpty
    @Size(max = 50)
    private String name;

    @NotEmpty
    @Size(max = 50)
    private String surname;

    @NotEmpty
    @CPF(message = "O CPF informado é inválido.")
    @Size(min = 11, max = 11)
    private String cpf;

    @NotNull
    @Past(message = "A data de nascimento informada deve ser anterior ao dia atual.")
    private LocalDate birthDate;

    @NotEmpty
    @Email
    @Size(max = 80)
    private String email;

    @NotEmpty
    private String password;

    private LocalDateTime registrationDate;

    @Size(max = 11)
    private String cellPhone;

    @NotNull
    private GenderDTO gender;

    private List<AddressDTO> addresses;

    private String resetPasswordToken;

}

