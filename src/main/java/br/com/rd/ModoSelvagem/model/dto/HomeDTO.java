package br.com.rd.ModoSelvagem.model.dto;


import lombok.Data;

import java.util.List;

@Data
public class HomeDTO {
    private CategoryDTO category;
    private List<ProductCardDTO> products;
}
