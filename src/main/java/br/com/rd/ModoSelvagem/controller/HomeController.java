package br.com.rd.ModoSelvagem.controller;

import br.com.rd.ModoSelvagem.model.dto.CategoryDTO;
import br.com.rd.ModoSelvagem.model.dto.HomeDTO;
import br.com.rd.ModoSelvagem.model.dto.ProductCardDTO;
import br.com.rd.ModoSelvagem.model.dto.ProductDTO;
import br.com.rd.ModoSelvagem.service.CategoryService;
import br.com.rd.ModoSelvagem.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;

    @GetMapping("/products")
    public List<HomeDTO> findHomeProducts() {
        return productService.findHomeProductsByCategory();
    }

    @GetMapping("/categories")
    public List<CategoryDTO> findHomeCategories(){
        return categoryService.findAllCategory();
    }
}
