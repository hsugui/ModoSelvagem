package br.com.rd.ModoSelvagem.controller;

import br.com.rd.ModoSelvagem.model.dto.*;
import br.com.rd.ModoSelvagem.service.AddressService;
import br.com.rd.ModoSelvagem.service.CustomerService;
import br.com.rd.ModoSelvagem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    CustomerService customerService;

    @Autowired
    OrderService orderService;

    @Autowired
    AddressService addressService;

    // Customer

    @GetMapping("/account/{id}")
    public CustomerDTO searchById(@PathVariable("id") Long id) {
        return customerService.searchCustomerById(id);
    }

    @PutMapping("/account/{id}")
    public CustomerDTO updateById(@RequestBody CustomerDTO dto, @PathVariable("id") Long id) {
        return customerService.updateById(dto,id);
    }

    @DeleteMapping("/account/{id}")
    public void deleteById(@PathVariable("id") Long id){
        customerService.deleteById(id);
    }

    // Orders

    @GetMapping("/account/{id}/orders")
    public List<OrderCardDTO> findByCustomerId(@PathVariable("id") Long id) {
        return orderService.findByCustomerId(id);
    }

    @GetMapping("/account/{customerId}/orders/{orderId}")
    public OrderDTO findByCustomerIdAndOrder(@PathVariable("customerId") Long customerId, @PathVariable("orderId") Long orderId) {
        return orderService.findByCustomerIdAndId(customerId, orderId);
    }

    // Addresses

    @PostMapping("/account/{customerId}/addresses")
    public AddressDTO createAddress(@PathVariable("customerId") Long customerId, @RequestBody AddressDTO address){
        return addressService.createAddress(address, customerId);
    }

    @GetMapping("/account/{customerId}/addresses")
    public CustomerAddressesDTO findAllAddresses (@PathVariable("customerId") Long customerId) {
        return addressService.findAllCustomerAddresses(customerId);
    }

    @GetMapping("/account/{customerId}/addresses/{addressId}")
    public AddressDTO findAddressByAddress(@PathVariable("customerId") Long customerId, @PathVariable("addressId") Long addressId) {
        return addressService.findCustomerAddressById(customerId, addressId);
    }

    @PutMapping("/account/{customerId}/addresses/{addressId}")
    public AddressDTO updateAddress(@PathVariable("customerId") Long customerId, @PathVariable("addressId") Long addressId, @RequestBody AddressDTO dto){
        return addressService.updateById(dto, customerId, addressId);
    }

    @DeleteMapping("/account/{customerId}/addresses/{addressId}")
    public void deleteById(@PathVariable("customerId") Long customerId, @PathVariable("addressId") Long addressId){
        addressService.deleteById(customerId, addressId);
    }


}
