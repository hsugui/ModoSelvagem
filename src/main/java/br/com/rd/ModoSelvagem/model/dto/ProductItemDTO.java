package br.com.rd.ModoSelvagem.model.dto;

import br.com.rd.ModoSelvagem.model.entity.ProductImage;
import br.com.rd.ModoSelvagem.model.entity.SalePrice;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductItemDTO {
    private Long id;
    private String name;
    private String shortDescription;
    private String fullDescription;
    private String weight;
    private String material;
    private String color;
    private String dimensions;
    private String warranty;
    private BrandDTO brand;
    private SubcategoryResponseDTO subcategory;
    private List<ProductImageDTO> images = new ArrayList<>();
    private Double price;
    private Integer storage;
}
