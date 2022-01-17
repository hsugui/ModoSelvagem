package br.com.rd.ModoSelvagem.controller;

import br.com.rd.ModoSelvagem.model.dto.ContactDTO;
import br.com.rd.ModoSelvagem.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/contact")
public class ContactController {
    @Autowired
    ContactService contactService;

    @PostMapping
    public ContactDTO create(@RequestBody @Valid ContactDTO contact) {
        return contactService.create(contact);
    }

    @GetMapping
    public List<ContactDTO> findAll() {
        return contactService.findAll();
    }

    @GetMapping("/{id}")
    public ContactDTO findById(@PathVariable("id") Long id) {
        return contactService.findById(id);
    }

    @GetMapping("/searchByEmail/{email}")
    public List<ContactDTO> findAllByEmail(@PathVariable("email") String email) {
        return contactService.findAllByEmail(email);
    }

    @GetMapping("/searchByDate/{date}")
    public List<ContactDTO> findAllByDate(@PathVariable("date") Date date) {
        return contactService.findAllByDate(date);
    }

    @PutMapping("/{id}")
    public ContactDTO updateById(@RequestBody ContactDTO dto, @PathVariable("id") Long id) {
        return contactService.updateById(dto, id);
    }
}
