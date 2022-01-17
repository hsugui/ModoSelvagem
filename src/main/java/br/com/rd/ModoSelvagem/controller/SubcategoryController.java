package br.com.rd.ModoSelvagem.controller;

import br.com.rd.ModoSelvagem.model.dto.SubcategoryRequestDTO;
import br.com.rd.ModoSelvagem.model.dto.SubcategoryResponseDTO;
import br.com.rd.ModoSelvagem.service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subcategories")
public class SubcategoryController {

    @Autowired
    SubcategoryService subcategoryService;

    @PostMapping
    public SubcategoryResponseDTO addSubcategory(@RequestBody SubcategoryRequestDTO dto) {
        return subcategoryService.addSubcategory(dto);
    }

    @GetMapping
    public List<SubcategoryResponseDTO> findAllSubcategories() {
        return subcategoryService.findAllSubcategories();
    }
}
