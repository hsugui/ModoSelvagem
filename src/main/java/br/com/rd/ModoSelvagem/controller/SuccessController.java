package br.com.rd.ModoSelvagem.controller;

import br.com.rd.ModoSelvagem.model.dto.OrderDTO;
import br.com.rd.ModoSelvagem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/success")
public class SuccessController {

    @Autowired
    OrderService orderService;

    @GetMapping("/{id}")
    public OrderDTO findById(@PathVariable("id") Long id) {
        return orderService.findOrderById(id);
    }

}
