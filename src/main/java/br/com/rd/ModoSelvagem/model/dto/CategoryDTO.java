package br.com.rd.ModoSelvagem.model.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryDTO {

    private Long id;
    private String categoryName;
//    private List<SubcategoryResponseDTO> subcategories = new ArrayList<>();

}
