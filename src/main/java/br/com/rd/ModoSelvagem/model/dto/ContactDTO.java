package br.com.rd.ModoSelvagem.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ContactDTO {

    private Long id;

    @NotNull @NotEmpty
    private String name;

    @NotNull @NotEmpty @Email
    private String email;

    @NotNull @NotEmpty
    private String subject;

    @NotNull @NotEmpty
    private String message;

    private LocalDateTime date;

}
