package br.com.rd.ModoSelvagem.model.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class EmailDTO {

    @NotBlank
    private String ownerRef; // ID do usuário receptor do e-mail

    @NotBlank
    @Email
    private String emailFrom; //de

    @NotBlank
    @Email
    private String emailTo; // para

    @NotBlank
    private String subject; // asssunto

    @NotBlank
    private String text; // corpo do e-mail

}
