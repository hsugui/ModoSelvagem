package br.com.rd.ModoSelvagem.service;

import br.com.rd.ModoSelvagem.enums.StatusEmail;
import br.com.rd.ModoSelvagem.model.dto.*;
import br.com.rd.ModoSelvagem.model.embeddable.ItemOrderId;
import br.com.rd.ModoSelvagem.model.entity.*;
import br.com.rd.ModoSelvagem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ItemOrderRepository itemOrderRepository;

    @Autowired
    OrderStatusRepository orderStatusRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    ProductService productService;

    @Autowired
    AddressService addressService;


    public OrderDTO createOrder(OrderDTO orderDTO) throws Exception {
        Order order = dtoToBusiness(orderDTO);
        order.setOrderDate(LocalDateTime.now());

        if (order.getStatus() != null) {
            Long idStatus = order.getStatus().getId();
            OrderStatus os;
            if (idStatus != null) {
                os = orderStatusRepository.getById(idStatus);
                order.setStatus(os);
            } else {
                throw new IllegalArgumentException("ID não pode ser nulo.");
            }
        } else {
            throw new IllegalArgumentException("Status do pedido não pode ser nulo.");
        }

        if (order.getPaymentMethod() != null) {
            Long idPM = order.getPaymentMethod().getId();
            PaymentMethod pm;
            if (idPM != null) {
                pm = paymentMethodRepository.getById(idPM);
                order.setPaymentMethod(pm);
            } else {
                throw new IllegalArgumentException("ID não pode ser nulo.");
            }
        } else {
            throw new IllegalArgumentException("Forma de pagamento não pode ser nula.");
        }

        if (order.getCustomer() != null) {
            Long idC = order.getCustomer().getId();
            Customer c;
            if (idC != null) {
                c = customerRepository.getById(idC);
                order.setCustomer(c);
            } else {
                throw new IllegalArgumentException("ID não pode ser nulo.");
            }
        } else {
            throw new IllegalArgumentException("Customer não pode ser nulo.");
        }

        if (order.getAddress() != null) {
            Long idAddress = order.getAddress().getId();
            Address address;
            if (idAddress != null) {
                address = addressRepository.getById(idAddress);
                order.setAddress(address);
            } else {
                throw new IllegalArgumentException("ID não pode ser nulo.");
            }
        } else {
            throw new IllegalArgumentException("Endereço não pode ser nulo.");
        }

        order = orderRepository.save(order);

        List<ItemOrder> busItems = new ArrayList<>();
        if (orderDTO.getItems() != null) {
            List<ItemOrderDTO> items = orderDTO.getItems();

            for (ItemOrderDTO itemOrderDTO : items) {
                ItemOrder itemOrder = new ItemOrder();
                itemOrder.setOrderId(order.getId());
                itemOrder.setProductId(itemOrderDTO.getProductId());
                itemOrder.setDiscount(itemOrderDTO.getDiscount());
                itemOrder.setQuantity(itemOrderDTO.getQuantity());
                itemOrder.setGrossValue(itemOrderDTO.getGrossValue());
                itemOrder.setNetValue(itemOrderDTO.getNetValue());
                itemOrderRepository.save(itemOrder);
                productService.subtractProductStorage(itemOrder.getProductId(), itemOrder.getQuantity());
                busItems.add(itemOrder);
            }
        }

        sendCheckoutEmailWithMime(order, order.getCustomer());

        invoiceService.createInvoice(order, busItems);

        return businessToDtoTest(order, busItems);
    }

    public List<OrderCardDTO> findAllOrdersCard() {
        List<Order> all = orderRepository.findAll();
        return listCardToDto(all);
    }

    public List<OrderCardDTO> findByCustomerId(Long id) {
        List<Order> obj = orderRepository.findByCustomerId(id);
        return listCardToDto(obj);
    }

    public OrderDTO findByCustomerIdAndId(Long customerId, Long orderId) {
        Order obj = orderRepository.findByCustomerIdAndId(customerId, orderId);
        List<ItemOrder> ioList = new ArrayList<>();
        for (Product p: obj.getItems()) {
            ItemOrderId ioId = new ItemOrderId(p.getId(), obj.getId());
            if (itemOrderRepository.existsById(ioId)) {
                Optional<ItemOrder> opiO = itemOrderRepository.findById(ioId);
                ioList.add(opiO.get());
            }
        }
        return businessToDtoTest(obj, ioList);
    }

    public List<OrderDTO> findAllOrders() {
        List<Order> all = orderRepository.findAll();
        return listToDto(all);
    }

    public OrderDTO findOrderById(Long id) {
        Optional<Order> o = orderRepository.findById(id);
        if (o.isPresent()){
            List<ItemOrder> ioList = new ArrayList<>();
            for (Product p: o.get().getItems()) {
                ItemOrderId ioId = new ItemOrderId(p.getId(), o.get().getId());
                if (itemOrderRepository.existsById(ioId)) {
                    Optional<ItemOrder> opiO = itemOrderRepository.findById(ioId);
                    ioList.add(opiO.get());
                }
            }
            return businessToDtoTest(o.get(), ioList);
        }
        throw new IllegalArgumentException("Id inexistente.");
    }

    private Order dtoToBusiness(OrderDTO dto) {
        Order business = new Order();
        business.setOrderDate(dto.getOrderDate());
        business.setTotalPrice(dto.getTotalPrice());
        business.setInstallmentsNumber(dto.getInstallmentsNumber());
        business.setInstallmentsValue(dto.getInstallmentsValue());
        business.setDeliveryType(dto.getDeliveryType());
        business.setShippingValue(dto.getShippingValue());
        business.setDeliveryTime(dto.getDeliveryTime());

        if (dto.getStatus() != null) {
            OrderStatus o = new OrderStatus();
            if (dto.getStatus().getId() != null) {
                o.setId(dto.getStatus().getId());
            } else {
                o.setDescription(dto.getStatus().getDescription());
            }
            business.setStatus(o);
        }

        if (dto.getPaymentMethod() != null) {
            PaymentMethod p = new PaymentMethod();
            if (dto.getPaymentMethod().getId() != null) {
                p.setId(dto.getPaymentMethod().getId());
            } else {
                p.setPaymentMethodName(dto.getPaymentMethod().getPaymentMethodName());
            }
            business.setPaymentMethod(p);
        }

        if (dto.getAddress() != null) {
            Address address = new Address();
            if (dto.getAddress().getId() != null) {
                address.setId(dto.getAddress().getId());
            } else {
                address.setCep(dto.getAddress().getCep());
                address.setStreet(dto.getAddress().getStreet());
                address.setNumber(dto.getAddress().getNumber());
                address.setComplement(dto.getAddress().getComplement());
                address.setDistrict(dto.getAddress().getDistrict());
                address.setCity(dto.getAddress().getCity());
                address.setUf(dto.getAddress().getUf());
            }
            business.setAddress(address);
        }

        if (dto.getCustomer() != null) {
            Customer c = new Customer();
            if (dto.getCustomer().getId() != null) {
                c.setId(dto.getCustomer().getId());
            } else {
                c.setName(dto.getCustomer().getName());
                c.setSurname(dto.getCustomer().getSurname());
                c.setCpf(dto.getCustomer().getCpf());
                c.setEmail(dto.getCustomer().getEmail());
                c.setAddresses(addressService.listToBusiness(dto.getCustomer().getAddresses()));
            }
            business.setCustomer(c);
        }

        return business;
    }

    private OrderDTO businessToDto(Order business) {
        OrderDTO dto = new OrderDTO();
        dto.setId(business.getId());
        dto.setOrderDate(business.getOrderDate());
        dto.setTotalPrice(business.getTotalPrice());
        dto.setInstallmentsNumber(business.getInstallmentsNumber());
        dto.setInstallmentsValue(business.getInstallmentsValue());
        dto.setDeliveryType(business.getDeliveryType());
        dto.setShippingValue(business.getShippingValue());
        dto.setDeliveryTime(business.getDeliveryTime());

        if (business.getStatus() != null) {
            OrderStatusDTO orderStatusDTO = new OrderStatusDTO();
            orderStatusDTO.setId(business.getStatus().getId());
            orderStatusDTO.setDescription(business.getStatus().getDescription());
            dto.setStatus(orderStatusDTO);
        }

        if (business.getPaymentMethod() != null) {
            PaymentMethodDTO pDto = new PaymentMethodDTO();
            pDto.setId(business.getPaymentMethod().getId());
            pDto.setPaymentMethodName(business.getPaymentMethod().getPaymentMethodName());
            dto.setPaymentMethod(pDto);
        }

        if (business.getAddress() != null) {
            AddressDTO aDto= new AddressDTO();
            aDto.setId(business.getAddress().getId());
            aDto.setCep(business.getAddress().getCep());
            aDto.setStreet(business.getAddress().getStreet());
            aDto.setNumber(business.getAddress().getNumber());
            aDto.setComplement(business.getAddress().getComplement());
            aDto.setDistrict(business.getAddress().getDistrict());
            aDto.setCity(business.getAddress().getCity());
            aDto.setUf(business.getAddress().getUf());
            dto.setAddress(aDto);
        }

        if (business.getCustomer() != null) {
            CustomerCheckoutDTO cDto = new CustomerCheckoutDTO();
            cDto.setId(business.getCustomer().getId());
            cDto.setName(business.getCustomer().getName());
            cDto.setSurname(business.getCustomer().getSurname());
            cDto.setCpf(business.getCustomer().getCpf());
            cDto.setEmail(business.getCustomer().getEmail());
            cDto.setAddresses(addressService.listToDTO(business.getCustomer().getAddresses()));
            dto.setCustomer(cDto);
        }

        return dto;
    }

    private OrderDTO businessToDtoTest(Order business, List<ItemOrder> ioList) {
        OrderDTO dto = new OrderDTO();
        dto.setId(business.getId());
        dto.setOrderDate(business.getOrderDate());
        dto.setTotalPrice(business.getTotalPrice());
        dto.setInstallmentsNumber(business.getInstallmentsNumber());
        dto.setInstallmentsValue(business.getInstallmentsValue());
        dto.setDeliveryType(business.getDeliveryType());
        dto.setShippingValue(business.getShippingValue());
        dto.setDeliveryTime(business.getDeliveryTime());

        if (business.getStatus() != null) {
            OrderStatusDTO orderStatusDTO = new OrderStatusDTO();
            orderStatusDTO.setId(business.getStatus().getId());
            orderStatusDTO.setDescription(business.getStatus().getDescription());
            dto.setStatus(orderStatusDTO);
        }

        if (business.getPaymentMethod() != null) {
            PaymentMethodDTO pDto = new PaymentMethodDTO();
            pDto.setId(business.getPaymentMethod().getId());
            pDto.setPaymentMethodName(business.getPaymentMethod().getPaymentMethodName());
            dto.setPaymentMethod(pDto);
        }

        if (business.getAddress() != null) {
            AddressDTO aDto= new AddressDTO();
            aDto.setId(business.getAddress().getId());
            aDto.setCep(business.getAddress().getCep());
            aDto.setStreet(business.getAddress().getStreet());
            aDto.setNumber(business.getAddress().getNumber());
            aDto.setComplement(business.getAddress().getComplement());
            aDto.setDistrict(business.getAddress().getDistrict());
            aDto.setCity(business.getAddress().getCity());
            aDto.setUf(business.getAddress().getUf());
            dto.setAddress(aDto);
        }

        if (business.getCustomer() != null) {
            CustomerCheckoutDTO cDto = new CustomerCheckoutDTO();
            cDto.setId(business.getCustomer().getId());
            cDto.setName(business.getCustomer().getName());
            cDto.setSurname(business.getCustomer().getSurname());
            cDto.setCpf(business.getCustomer().getCpf());
            cDto.setEmail(business.getCustomer().getEmail());
            cDto.setAddresses(addressService.listToDTO(business.getCustomer().getAddresses()));
            dto.setCustomer(cDto);
        }

        List<ItemOrderDTO> ioDtoList = new ArrayList<>();
        for (ItemOrder io: ioList) {
            Long productId = io.getProductId();
            ProductItemDTO productById = productService.findProductById(productId);

            ItemOrderDTO ioDto = new ItemOrderDTO();
            ioDto.setOrderId(io.getOrderId());
            ioDto.setProductId(io.getProductId());
            ioDto.setDiscount(io.getDiscount());
            ioDto.setQuantity(io.getQuantity());
            ioDto.setNetValue(io.getNetValue());
            ioDto.setGrossValue(io.getGrossValue());
            ioDto.setProductImage(productById.getImages().get(0));
            ioDto.setProductName(productById.getName());

            ioDtoList.add(ioDto);
        }
        dto.setItems(ioDtoList);

        return dto;
    }


    private OrderCardDTO businessOrderCardToDTO(Order order) {
        OrderCardDTO orderCard = new OrderCardDTO();
        orderCard.setId(order.getId());
        orderCard.setOrderDate(order.getOrderDate());
        orderCard.setTotalPrice(order.getTotalPrice());
        OrderStatusDTO statusDTO = new OrderStatusDTO();
        statusDTO.setId(order.getStatus().getId());
        statusDTO.setDescription(order.getStatus().getDescription());
        orderCard.setStatus(statusDTO);

        return orderCard;
    }

    private List<OrderCardDTO> listCardToDto(List<Order> list) {
        List<OrderCardDTO> listCardDto = new ArrayList<>();
        for (Order o : list) {
            listCardDto.add(businessOrderCardToDTO(o));
        }
        return listCardDto;
    }

    private AddressDTO addressToDTO(Address address) {
        AddressDTO aDto = new AddressDTO();
        aDto.setId(address.getId());
        aDto.setCep(address.getCep());
        aDto.setCity(address.getCity());
        aDto.setComplement(address.getComplement());
        aDto.setDistrict(address.getDistrict());
        aDto.setNumber(address.getNumber());
        aDto.setStreet(address.getStreet());
        aDto.setUf(address.getUf());
        return aDto;
    }

    private Address addressToBusiness(AddressDTO aDto) {
        Address address = new Address();
        address.setId(aDto.getId());
        address.setCep(aDto.getCep());
        address.setCity(aDto.getCity());
        address.setComplement(aDto.getComplement());
        address.setDistrict(aDto.getDistrict());
        address.setNumber(aDto.getNumber());
        address.setStreet(aDto.getStreet());
        address.setUf(aDto.getUf());
        return address;
    }

    private List<OrderDTO> listToDto(List<Order> list) {
        List<OrderDTO> listDto = new ArrayList<>();
        for (Order o : list) {
            listDto.add(businessToDto(o));
        }
        return listDto;
    }

    // ENVIO DE E-MAIL DE CONFIRMAÇÃO DE PEDIDO
    @Async
    private void sendCheckoutEmailWithMime(Order order, Customer customer) {
        String mailContent = "<center><a href='http://localhost:3000/home'><img src='cid:logoImage' /></a><center>";
        mailContent += "<center><h1 style='color: #392A22'>Olá, " + customer.getName() + "!</h1></center>";
        mailContent += "<center><h2 style='color: #392A22'>O pedido #2021" + order.getId() + " foi confirmado.</h2></center>";
        mailContent += "<center><h3 style='color: #392A22'>Em breve você receberá um e-mail com mais informações. Agradecemos a preferência.</h3><center>";
        mailContent += "<center><h4 style='color: #392A22'>Fique atento às nossas novidades! <a href='/http://localhost/home'></a></h4><center>";
        mailContent += "<hr>";
        mailContent += "<center><h5 style='color: #838383'>© Modo Selvagem Ltda. | Todos os direitos reservados</h5><center>";
        mailContent += "<center><h6 style='color: #838383'>CNPJ: 00.000.000/0001-02 | Desde 2021</h6><center>";
        mailContent += "<center><h6 style='color: #838383'>Contato: lojamodoselvagem@gmail.com</h6><center>";

        Email email = new Email();
        email.setSendDateEmail(LocalDateTime.now());
        email.setOwnerRef(customer.getId());
        email.setEmailTo(customer.getEmail());
        email.setEmailFrom("lojamodoselvagem@gmail.com");
        email.setSubject(String.format("Pedido #2021%d confirmado", order.getId()));
        email.setText(mailContent);

        try{
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(email.getEmailFrom(), "Loja Modo Selvagem");
            helper.setTo(email.getEmailTo());
            helper.setSubject(email.getSubject());
            helper.setText(mailContent, true);

            ClassPathResource resource = new ClassPathResource("/images/logoAtualizado.png");
            helper.addInline("logoImage", resource);

            emailSender.send(message);
            email.setStatusEmail(StatusEmail.SENT);
        } catch (MailException | MessagingException | UnsupportedEncodingException e){
            email.setStatusEmail(StatusEmail.ERROR);
        } finally {
            emailRepository.save(email);
        }
    }

    //    private ItemOrderId ioConvert(ItemOrderIdDTO dto) {
//        ItemOrderId ioId = new ItemOrderId();
//        ioId.setItemId(dto.getItemId());
//        ioId.setOrderId(dto.getOrderId());
//        return ioId;
//    }
//
//    private ItemOrderIdDTO ioDtoConvert(ItemOrderId io) {
//        ItemOrderIdDTO ioDto = new ItemOrderIdDTO();
//        ioDto.setItemId(io.getItemId());
//        ioDto.setOrderId(io.getOrderId());
//        return ioDto;
//    }

}
