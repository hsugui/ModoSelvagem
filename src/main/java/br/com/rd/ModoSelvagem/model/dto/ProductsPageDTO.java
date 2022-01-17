package br.com.rd.ModoSelvagem.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductsPageDTO {

    private SubcategoryResponseDTO subcategory;
    private List<ProductCardDTO> products;
}
