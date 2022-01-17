package br.com.rd.ModoSelvagem.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductCardDTO {
    private Long id;
    private String name;
    private String image;
    private Double price;
    private Integer storage;
}


