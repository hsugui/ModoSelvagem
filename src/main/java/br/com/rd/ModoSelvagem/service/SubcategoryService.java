package br.com.rd.ModoSelvagem.service;

import br.com.rd.ModoSelvagem.model.dto.SubcategoryRequestDTO;
import br.com.rd.ModoSelvagem.model.dto.SubcategoryResponseDTO;
import br.com.rd.ModoSelvagem.model.entity.Category;
import br.com.rd.ModoSelvagem.model.entity.Subcategory;
import br.com.rd.ModoSelvagem.repository.CategoryRepository;
import br.com.rd.ModoSelvagem.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubcategoryService {

    @Autowired
    SubcategoryRepository subcategoryRepository;

    @Autowired
    CategoryRepository categoryRepository;

    private Subcategory dtoToBusiness(SubcategoryRequestDTO dto) {
        Subcategory business = new Subcategory();
        business.setSubcategoryName(dto.getSubcategoryName());
        if (dto.getCategory() != null) {
            Category cat = new Category();
            if (dto.getCategory().getId() != null) {
                cat.setId(dto.getCategory().getId());
            } else {
                cat.setCategoryName(dto.getCategory().getCategoryName());
            }
            business.setCategory(cat);
        }
        return business;
    }

    public SubcategoryResponseDTO businessToDto(Subcategory business) {
        SubcategoryResponseDTO dto = new SubcategoryResponseDTO();
        dto.setId(business.getId());
        dto.setSubcategoryName(business.getSubcategoryName());
        return dto;
    }

    private List<SubcategoryResponseDTO> listToDto(List<Subcategory> list) {
        List<SubcategoryResponseDTO> listDto = new ArrayList<>();
        for (Subcategory s : list) {
            listDto.add(businessToDto(s));
        }
        return listDto;
    }

    public SubcategoryResponseDTO addSubcategory(SubcategoryRequestDTO dto) {
        Subcategory sub = this.dtoToBusiness(dto);


        if (sub.getCategory() != null) {
            Long id = sub.getCategory().getId();
            if (id != null) {
                Category cat = categoryRepository.getById(id);
                sub.setCategory(cat);
            }
        }
        sub = subcategoryRepository.save(sub);
        return businessToDto(sub);
    }

    public List<SubcategoryResponseDTO> findAllSubcategories() {
        return listToDto(subcategoryRepository.findAll());
    }

}
