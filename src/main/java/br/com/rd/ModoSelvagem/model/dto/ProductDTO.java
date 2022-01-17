package br.com.rd.ModoSelvagem.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ProductDTO {

    private Long id;
    private String name;
    private String shortDescription;
    private String fullDescription;
    private String weight;
    private String material;
    private String color;
    private String dimensions;
    private String warranty;
    private LocalDateTime updateDate;
    private BrandDTO brand;
    private SubcategoryResponseDTO subcategory;
    private List<ProductImageDTO> images = new ArrayList<>();
    private List<SalePriceDTO> prices = new ArrayList<>();
    private StorageDTO storage;

}
