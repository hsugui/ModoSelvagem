package br.com.rd.ModoSelvagem.model.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity(name = "tb_contact")
@Data

public class Contact<format> {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, columnDefinition = "VARCHAR(100)")
        private String name;

        @Column(nullable = false, columnDefinition = "VARCHAR(100)")
        private String email;

        @Column(nullable = false, columnDefinition = "VARCHAR(50)")
        private String subject;

        @Column(nullable = false, columnDefinition = "VARCHAR(1000)")
        private String message;

        @Column(nullable = false)
        private LocalDateTime date;

}
