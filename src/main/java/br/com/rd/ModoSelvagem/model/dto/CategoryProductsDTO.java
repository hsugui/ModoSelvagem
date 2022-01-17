package br.com.rd.ModoSelvagem.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryProductsDTO {
    private String category;
    private List<ProductCardDTO> products;
}
