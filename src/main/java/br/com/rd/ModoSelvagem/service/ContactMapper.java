package br.com.rd.ModoSelvagem.service;

import br.com.rd.ModoSelvagem.model.dto.ContactDTO;
import br.com.rd.ModoSelvagem.model.entity.Contact;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ContactMapper {

    public List<ContactDTO> listToDto(List<Contact> list){
        List<ContactDTO> listDTO = new ArrayList<ContactDTO>();
        for (Contact c : list){
            listDTO.add(this.businessToDTO(c));
        }
        return listDTO;
    }


    public Contact dtoToBusiness(ContactDTO dto){
        Contact business = new Contact();
        business.setName(dto.getName());
        business.setEmail(dto.getEmail());
        business.setSubject(dto.getSubject());
        business.setMessage(dto.getMessage());
        business.setDate(dto.getDate());
        return business;
    }

    public ContactDTO businessToDTO(Contact business){
        ContactDTO dto = new ContactDTO();
        dto.setId(business.getId());
        dto.setName(business.getName());
        dto.setEmail(business.getEmail());
        dto.setSubject(business.getSubject());
        dto.setMessage(business.getMessage());
        dto.setDate(business.getDate());
        return dto;
    }
}
