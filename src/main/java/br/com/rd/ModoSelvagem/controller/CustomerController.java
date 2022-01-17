package br.com.rd.ModoSelvagem.controller;

import br.com.rd.ModoSelvagem.model.dto.CustomerDTO;
import br.com.rd.ModoSelvagem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/customers")

public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping
    public CustomerDTO register(@RequestBody @Valid CustomerDTO customer){
        return customerService.registerCustomer(customer);
    }

    @GetMapping
    public List<CustomerDTO> getAll(){
        return customerService.getAllCustomer();
    }

    @GetMapping("/{id}")
    public CustomerDTO searchById(@PathVariable("id") Long id) {
        return customerService.searchCustomerById(id);
    }

    @PutMapping("/{id}")
    public CustomerDTO updateById(@RequestBody CustomerDTO dto, @PathVariable("id") Long id) {
        return customerService.updateById(dto,id);
    }

}



