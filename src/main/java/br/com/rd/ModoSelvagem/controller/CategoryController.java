package br.com.rd.ModoSelvagem.controller;

import br.com.rd.ModoSelvagem.model.dto.CategoryDTO;
import br.com.rd.ModoSelvagem.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CategoryDTO create(@RequestBody CategoryDTO dto) {
        return categoryService.createCategory(dto);
    }

    @GetMapping
    public List<CategoryDTO> findAll() {
        return categoryService.findAllCategory();
    }

    @GetMapping("/{id}")
    public CategoryDTO searchById(@PathVariable("id") Long id) {
        return categoryService.searchCategoryById(id);
    }

    @PutMapping("/{id}")
    public CategoryDTO updateById(@RequestBody CategoryDTO dto, @PathVariable("id") Long id) {
        return categoryService.updateById(dto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") Long id) {
        categoryService.deleteById(id);
    }

}
