package br.com.rd.ModoSelvagem.controller;

import br.com.rd.ModoSelvagem.model.dto.*;
import br.com.rd.ModoSelvagem.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ProductDTO create(@RequestBody ProductDTO productDTO) {
        return productService.createProduct(productDTO);
    }

    @GetMapping
    public List<ProductDTO> findAll() {
        return productService.findAllProducts();
    }

    @GetMapping("/search")
    public List<ProductCardDTO> findByProductNameOrDescription(@RequestParam("query") String text) {
        return productService.findByProductNameOrDescription(text.replaceAll("\\s", "%"));
    }

    @GetMapping("/{id}")
    public ProductItemDTO findById(@PathVariable("id") Long id) {
        return productService.findProductById(id);
    }

    @GetMapping("/subcategory/{id}")
    public ProductsPageDTO findBySubcategory(@PathVariable("id")Long id){
        return productService.findProductsBySubcategory(id);
    }

    @GetMapping("/category/{id}")
    public CategoryProductsDTO findByCategory(@PathVariable("id") Long id){
        return productService.findProductsAndCategoryNameByCategoryId(id);

    }
}
