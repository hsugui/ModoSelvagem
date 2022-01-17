package br.com.rd.ModoSelvagem.controller;

import br.com.rd.ModoSelvagem.model.dto.OrderDTO;
import br.com.rd.ModoSelvagem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/checkout")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public OrderDTO create(@RequestBody @Valid OrderDTO orderDTO) throws Exception {
        return orderService.createOrder(orderDTO);
    }

    @GetMapping
    public List<OrderDTO> findAll() {
        return orderService.findAllOrders();
    }

}
