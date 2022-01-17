package br.com.rd.ModoSelvagem.service;

import br.com.rd.ModoSelvagem.model.dto.InvoiceDTO;
import br.com.rd.ModoSelvagem.model.dto.InvoiceItemDTO;
import br.com.rd.ModoSelvagem.model.embeddable.InvoiceItemId;
import br.com.rd.ModoSelvagem.model.entity.*;
import br.com.rd.ModoSelvagem.repository.InvoiceItemRepository;
import br.com.rd.ModoSelvagem.repository.InvoiceRepository;
import br.com.rd.ModoSelvagem.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    InvoiceItemRepository invoiceItemRepository;

    @Autowired
    ProductRepository productRepository;

    public void createInvoice (Order order, List<ItemOrder> orderItems) {
        Invoice invoice = orderToInvoice(order);
        List<InvoiceItem> invoiceItems = orderItemsToInvoiceItems(orderItems);
        invoice.setTotalCofins(calculateTotalCofins(invoiceItems));
        invoice.setTotalIcms(calculateTotalIcms(invoiceItems));
        invoice.setTotalIpi(calculateTotalIpi(invoiceItems));
        invoice.setTotalPis(calculateTotalPis(invoiceItems));
        invoice = invoiceRepository.save(invoice);

        for (InvoiceItem item: invoiceItems) {
            item.setInvoiceId(invoice.getId());
            invoiceItemRepository.save(item);
        }
    }

    public InvoiceDTO findInvoiceById(Long id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        if (invoice.isPresent()) {
            List<InvoiceItem> items = new ArrayList<>();
            for (Product p: invoice.get().getItems()) {
                InvoiceItemId itemId = new InvoiceItemId(invoice.get().getId(), p.getId());
                Optional<InvoiceItem> item = invoiceItemRepository.findById(itemId);
                if (item.isPresent()) {
                    items.add(item.get());
                }
            }
            return businessInvoiceToDto(invoice.get(), items);
        }
        throw new IllegalArgumentException("Não foi encontrada uma invoice com esse ID.");
    }

    public InvoiceDTO findInvoiceByOrderId(Long id) {
        Optional<Invoice> invoice = invoiceRepository.findByOrderId(id);
        if (invoice.isPresent()) {
            List<InvoiceItem> items = new ArrayList<>();
            for (Product p: invoice.get().getItems()) {
                InvoiceItemId itemId = new InvoiceItemId(invoice.get().getId(), p.getId());
                Optional<InvoiceItem> item = invoiceItemRepository.findById(itemId);
                if (item.isPresent()) {
                    items.add(item.get());
                }
            }
            return businessInvoiceToDto(invoice.get(), items);
        }
        throw new IllegalArgumentException("Não foi encontrada uma nota deste pedido.");
    }

    private String generateInvoiceNumber() {
        Invoice lastInvoice = invoiceRepository.findLastInvoice();
        Integer nextInvoiceNumber;

        if (lastInvoice == null) {
            nextInvoiceNumber = 1000;
        } else {
            Integer invoiceNumber = Integer.parseInt(lastInvoice.getInvoiceNumber());
            nextInvoiceNumber = invoiceNumber + 1;
        }
        return nextInvoiceNumber.toString();
    }

    private Double calculateTax (Double pcTax, Double itemValue) {
        return (pcTax / 100) * itemValue;
    }

    private Double calculateTotalIcms(List<InvoiceItem> items) {
        Double total = 0.0;
        for (InvoiceItem item: items) {
            total += item.getIcmsAmount();
        }
        return total;
    }

    private Double calculateTotalCofins(List<InvoiceItem> items) {
        Double total = 0.0;
        for (InvoiceItem item: items) {
            total += item.getCofinsAmount();
        }
        return total;
    }

    private Double calculateTotalPis(List<InvoiceItem> items) {
        Double total = 0.0;
        for (InvoiceItem item: items) {
            total += item.getPisAmount();
        }
        return total;
    }

    private Double calculateTotalIpi(List<InvoiceItem> items) {
        Double total = 0.0;
        for (InvoiceItem item: items) {
            total += item.getIpiAmount();
        }
        return total;
    }

    private Double calculateTotalValue(List<InvoiceItem> items) {
        Double total = 0.0;
        for (InvoiceItem item: items) {
            total += item.getNetValue();
        }
        return total;
    }

    private Double calculateTotalPurchaseValue(Invoice invoice) {
        return invoice.getTotalProductsValue() + invoice.getShippingValue();
    }



    private Invoice orderToInvoice(Order order) {
        Invoice invoice = new Invoice();
        invoice.setAccessKey("3219 1105 5707 1400 0825 5500 1005 9146 6211 3308 2968");
        invoice.setCnpj("51.876.532/0001-28");
        invoice.setCompanyAdress("Av. Vilela, 18 – Tatuapé, São Paulo/SP, 06800-000");
        invoice.setCompanyEmail("lojamodoselvagem@gmail.com");
        invoice.setCompanyName("Modo Selvagem LTDA.");
        invoice.setCompanyPhone("(11) 94321-1234");
        invoice.setInvoiceNumber(generateInvoiceNumber());
        invoice.setInvoiceSequence("001");
        invoice.setIssueDate(LocalDateTime.now());
        invoice.setStateRegistration("055.267.843.671");
        invoice.setOrder(order);
        invoice.setCustomer(order.getCustomer());
        invoice.setCustomerName(order.getCustomer().getName());
        invoice.setCustomerSurname(order.getCustomer().getSurname());
        invoice.setShippingValue(order.getShippingValue());
        invoice.setTotalProductsValue(order.getTotalPrice() - order.getShippingValue());
        invoice.setTotalPurchaseValue(order.getTotalPrice());
        return invoice;
    }

    private List<InvoiceItem> orderItemsToInvoiceItems(List<ItemOrder> orderItems) {
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        for (ItemOrder io: orderItems) {
            Optional<Product> byId = productRepository.findById(io.getProductId());
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setProductId(io.getProductId());
            invoiceItem.setProductName(byId.get().getName());
            invoiceItem.setPcCofins(7.6);
            invoiceItem.setCofinsAmount(calculateTax(invoiceItem.getPcCofins(), io.getGrossValue()));
            invoiceItem.setPcIcms(17.5);
            invoiceItem.setIcmsAmount(calculateTax(invoiceItem.getPcIcms(), io.getGrossValue()));
            invoiceItem.setPcIpi(5.0);
            invoiceItem.setIpiAmount(calculateTax(invoiceItem.getPcIpi(), io.getGrossValue()));
            invoiceItem.setPcPis(1.65);
            invoiceItem.setPisAmount(calculateTax(invoiceItem.getPcPis(), io.getGrossValue()));
            invoiceItem.setQuantity(io.getQuantity());
            invoiceItem.setGrossValue(io.getGrossValue());
            invoiceItem.setNetValue(io.getNetValue());
            invoiceItem.setDiscount(io.getDiscount());
            invoiceItems.add(invoiceItem);
        }
        return invoiceItems;
    }

    private InvoiceDTO businessInvoiceToDto(Invoice invoice, List<InvoiceItem> items) {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setId(invoice.getId());
        dto.setAccessKey(invoice.getAccessKey());
        dto.setCnpj(invoice.getCnpj());
        dto.setCompanyAdress(invoice.getCompanyAdress());
        dto.setCompanyEmail(invoice.getCompanyEmail());
        dto.setCompanyName(invoice.getCompanyName());
        dto.setCompanyPhone(invoice.getCompanyPhone());
        dto.setInvoiceNumber(invoice.getInvoiceNumber());
        dto.setInvoiceSequence(invoice.getInvoiceSequence());
        dto.setIssueDate(invoice.getIssueDate());
        dto.setStateRegistration(invoice.getStateRegistration());
        dto.setTotalCofins(invoice.getTotalCofins());
        dto.setTotalIcms(invoice.getTotalIcms());
        dto.setTotalIpi(invoice.getTotalIpi());
        dto.setTotalPis(invoice.getTotalPis());
        dto.setCustomerId(invoice.getCustomer().getId());
        dto.setCustomerName(invoice.getCustomer().getName());
        dto.setCustomerSurname(invoice.getCustomer().getSurname());
        dto.setOrderId(invoice.getOrder().getId());
        dto.setShippingValue(invoice.getShippingValue()); // valor do frete
        dto.setTotalProductsValue(calculateTotalValue(items)); // soma do valor do total de itens
        dto.setTotalPurchaseValue(calculateTotalPurchaseValue(invoice)); // soma do frete + valor total dos itens
        dto.setItems(businessItemsToDto(items));
        return dto;
    }

    private List<InvoiceItemDTO> businessItemsToDto(List<InvoiceItem> items) {
        List<InvoiceItemDTO> itemsDTO = new ArrayList<>();
        for (InvoiceItem item: items) {
            Optional<Product> byId = productRepository.findById(item.getProductId());
            InvoiceItemDTO itemDTO = new InvoiceItemDTO();
            itemDTO.setInvoiceId(item.getInvoiceId());
            itemDTO.setProductId(item.getProductId());
            itemDTO.setProductName(byId.get().getName());
            itemDTO.setPcCofins(item.getPcCofins());
            itemDTO.setCofinsAmount(item.getCofinsAmount());
            itemDTO.setPcIcms(item.getPcIcms());
            itemDTO.setIcmsAmount(item.getIcmsAmount());
            itemDTO.setPcIpi(item.getPcIpi());
            itemDTO.setIpiAmount(item.getIpiAmount());
            itemDTO.setPcPis(item.getPcPis());
            itemDTO.setPisAmount(item.getPisAmount());
            itemDTO.setDiscount(item.getDiscount());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setGrossValue(item.getGrossValue());
            itemDTO.setNetValue(item.getNetValue());
            itemsDTO.add(itemDTO);
        }
        return itemsDTO;
    }
}
