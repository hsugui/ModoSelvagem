package br.com.rd.ModoSelvagem.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name= "TB_ADDRESS")
@Data
public class Address implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;
    @Column(nullable = false, columnDefinition = "CHAR(8)")
    private String cep;
    @Column(nullable = false, length = 100)
    private String street;
    @Column (nullable = false)
    private Integer number;
    @Column (length = 50)
    private String complement;
    @Column (nullable = false, length = 100)
    private String district;
    @Column (nullable = false, length = 100)
    private String city;
    @Column (nullable = false, columnDefinition = "CHAR(2)")
    private String uf;

}