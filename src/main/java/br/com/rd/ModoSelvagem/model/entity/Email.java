package br.com.rd.ModoSelvagem.model.entity;

import br.com.rd.ModoSelvagem.enums.StatusEmail;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TB_EMAIL")
public class Email implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long emailId;

    private Long ownerRef; // ID do usuário receptor do e-mail

    private String emailFrom; //de

    private String emailTo; // para

    private String subject; // asssunto

    @Column(columnDefinition = "TEXT") // para não limitar a 255 caracteres
    private String text; // corpo do e-mail

    private LocalDateTime sendDateEmail;

    private StatusEmail statusEmail;

}
