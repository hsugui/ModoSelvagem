package br.com.rd.ModoSelvagem.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity(name= "TB_GENDER")
@Data
public class Gender {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String description;
}