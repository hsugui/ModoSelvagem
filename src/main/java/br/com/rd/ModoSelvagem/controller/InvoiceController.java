package br.com.rd.ModoSelvagem.controller;

import br.com.rd.ModoSelvagem.model.dto.InvoiceDTO;
import br.com.rd.ModoSelvagem.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    @GetMapping("/{id}")
    public InvoiceDTO findInvoiceById(@PathVariable("id") Long id) {
        return invoiceService.findInvoiceById(id);
    }

    @GetMapping("/order/{id}")
    public InvoiceDTO findInvoiceByOrderId(@PathVariable("id") Long id) {
        return invoiceService.findInvoiceByOrderId(id);
    }
}
