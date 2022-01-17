package br.com.rd.ModoSelvagem.model.dto;

import br.com.rd.ModoSelvagem.model.entity.Category;
import lombok.Data;

@Data
public class SubcategoryRequestDTO {

    private Long id;
    private String subcategoryName;
    private CategoryDTO category;

}
