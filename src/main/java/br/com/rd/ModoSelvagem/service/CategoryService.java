package br.com.rd.ModoSelvagem.service;

import br.com.rd.ModoSelvagem.model.dto.CategoryDTO;
import br.com.rd.ModoSelvagem.model.dto.SubcategoryResponseDTO;
import br.com.rd.ModoSelvagem.model.entity.Category;
import br.com.rd.ModoSelvagem.model.entity.Subcategory;
import br.com.rd.ModoSelvagem.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public CategoryDTO createCategory(CategoryDTO dto){
        Category category = dtoToBusiness(dto);
        category = categoryRepository.save(category);
        return businessToDto(category);
    }

    public List<CategoryDTO> findAllCategory(){
        List<Category> all = categoryRepository.findAll();
        return  listToDto(all);
    }

    public CategoryDTO searchCategoryById(Long id) {
        Optional<Category> optional = categoryRepository.findById(id);
        if (optional.isPresent()) {
            return businessToDto(optional.get());
        }
        return null;
    }

    public CategoryDTO updateById(CategoryDTO dto, Long id) {
        Optional<Category> op = categoryRepository.findById(id);
        if (op.isPresent()) {
            Category obj = op.get();
            if (dto.getCategoryName() != null) {
                obj.setCategoryName(dto.getCategoryName());
            }
            categoryRepository.save(obj);
            return businessToDto(obj);
        }
        return null;
    }

    public void deleteById(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        }
    }

    private Category dtoToBusiness(CategoryDTO dto) {
        Category business = new Category();
        business.setCategoryName(dto.getCategoryName());
        return business;
    }

    public CategoryDTO businessToDto(Category business) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(business.getId());
        dto.setCategoryName(business.getCategoryName());
//        if (business.getSubcategories() != null) {
//            List<SubcategoryResponseDTO> subDtoList = new ArrayList<>();
//            for (Subcategory s: business.getSubcategories()) {
//                SubcategoryResponseDTO subdto = new SubcategoryResponseDTO();
//                subdto.setId(s.getId());
//                subdto.setSubcategoryName(s.getSubcategoryName());
//                subDtoList.add(subdto);
//            }
//
//            dto.setSubcategories(subDtoList);
//        }
        return dto;
    }

    private List<CategoryDTO> listToDto(List<Category> list) {
        List<CategoryDTO> listDto = new ArrayList<>();
        for (Category c : list) {
            listDto.add(businessToDto(c));
        }
        return listDto;
    }
}
