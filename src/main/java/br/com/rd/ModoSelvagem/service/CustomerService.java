package br.com.rd.ModoSelvagem.service;

import br.com.rd.ModoSelvagem.enums.StatusEmail;
import br.com.rd.ModoSelvagem.exceptions.CustomerNotFoundException;
import br.com.rd.ModoSelvagem.model.dto.AddressDTO;
import br.com.rd.ModoSelvagem.model.dto.CustomerDTO;
import br.com.rd.ModoSelvagem.model.dto.GenderDTO;
import br.com.rd.ModoSelvagem.model.entity.*;
import br.com.rd.ModoSelvagem.model.entity.Address;
import br.com.rd.ModoSelvagem.model.entity.Customer;
import br.com.rd.ModoSelvagem.model.entity.Gender;
import br.com.rd.ModoSelvagem.repository.AddressRepository;
import br.com.rd.ModoSelvagem.repository.CustomerAddressRepository;
import br.com.rd.ModoSelvagem.repository.CustomerRepository;
import br.com.rd.ModoSelvagem.repository.EmailRepository;
import br.com.rd.ModoSelvagem.repository.GenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import javax.transaction.Transactional;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    GenderRepository genderRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    AddressService addressService;

    @Autowired
    CustomerAddressRepository customerAddressRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public CustomerService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public CustomerDTO registerCustomer(CustomerDTO customer){
        Customer newCustomer = this.dtoToBusiness(customer);

        Optional<Customer> byEmail = customerRepository.findByEmail(newCustomer.getEmail());
        Optional<Customer> byCpf = customerRepository.findByCpf(newCustomer.getCpf());

        if (byEmail.isPresent()) {
            throw new IllegalStateException("E-mail já registrado.");
        }

        if (byCpf.isPresent()) {
            throw new IllegalStateException("CPF já registrado.");
        }

        if (newCustomer.getGender() != null) {
            Long id = newCustomer.getGender().getId();
            Gender gender;
            if (id != null) {
                gender = this.genderRepository.getById(id);
            } else{
                gender = this.genderRepository.save(newCustomer.getGender());
            }
            newCustomer.setGender(gender);
        }

        if (newCustomer.getAddresses() != null) {
            List<Address> addressesList = newCustomer.getAddresses();
            for (Address address : addressesList) {
                addressRepository.save(address);
            }
        }

        newCustomer.setRegistrationDate(LocalDateTime.now());
        newCustomer = customerRepository.save(newCustomer);
        
        sendCustomerEmailWithMime(newCustomer);

        return this.businessToDto(newCustomer);
    }

    public List<CustomerDTO> getAllCustomer(){
        List<Customer> listCustomer = customerRepository.findAll();
        return this.listToDTO(listCustomer);
    }

    private List<CustomerDTO> listToDTO(List<Customer> list){
        List<CustomerDTO> listDto = new ArrayList<>();
        for (Customer customer : list) {
            CustomerDTO customerDto = this.businessToDto(customer);
            listDto.add(customerDto);
        }
        return listDto;
    }

    public CustomerDTO searchCustomerById(Long id) {
        Optional<Customer> op = customerRepository.findById(id);

        if (op.isPresent()){
            return businessToDto(op.get());
        }
        return null;
    }

    @Transactional
    public CustomerDTO updateById(CustomerDTO dto, Long id){
        isDateValid(dto.getBirthDate());

        Optional<Customer> op = customerRepository.findById(id);

        if (op.isPresent()){
            Customer obj = op.get();

            if(dto.getName() != null){
                obj.setName(dto.getName());
            }
            if(dto.getSurname() != null){
                obj.setSurname(dto.getSurname());
            }
            if(dto.getCpf() != null){
                obj.setCpf(dto.getCpf());
            }
            if(dto.getBirthDate() != null){
                obj.setBirthDate(dto.getBirthDate());
            }
            if(dto.getEmail() != null){
                obj.setEmail(dto.getEmail());
            }
            if(dto.getPassword() != null){
                obj.setPassword(dto.getPassword());
            }
            if(dto.getCellPhone() != null){
                obj.setCellPhone(dto.getCellPhone());
            }
            if(dto.getResetPasswordToken() != null) {
                obj.setResetPasswordToken(dto.getResetPasswordToken());
            }
            if (dto.getGender() != null){
                Gender gender = new Gender();
                if (dto.getGender().getId() != null) {
                    if (genderRepository.existsById(dto.getGender().getId())){
                        gender = genderRepository.getById(dto.getGender().getId());
                    }
                } else {
                    gender.setDescription(dto.getGender().getDescription());
                    genderRepository.save(gender);
                }
                obj.setGender(gender);
            }

            if (dto.getAddresses() != null){
                List<Address> addressesList = addressService.listToBusiness(dto.getAddresses());
                for (Address address : addressesList) {
                    addressRepository.save(address);
                }
                obj.setAddresses(addressesList);
            }



//            if (dto.getAddress() != null){
//                Address address = new Address();
//                if (dto.getAddress().getId() != null) {
//                    if (addressRepository.existsById(dto.getAddress().getId())){
//                        address = addressRepository.getById(dto.getAddress().getId());
//                    }
//                } else {
//                    address.setCep(dto.getAddress().getCep());
//                    address.setStreet(dto.getAddress().getStreet());
//                    address.setNumber(dto.getAddress().getNumber());
//                    address.setComplement(dto.getAddress().getComplement());
//                    address.setDistrict(dto.getAddress().getDistrict());
//                    address.setCity(dto.getAddress().getCity());
//                    address.setUf(dto.getAddress().getUf());
//                    addressRepository.save(address);
//                }
//                obj.setAddress(address);
//            }

            customerRepository.save(obj);
            return  businessToDto(obj);
        }
        return null;
    }

    public Customer dtoToBusiness(CustomerDTO dto) {
        isDateValid(dto.getBirthDate());
        Customer business = new Customer();
        business.setName(dto.getName());
        business.setSurname(dto.getSurname());
        business.setCpf(dto.getCpf());
        business.setBirthDate(dto.getBirthDate());
        business.setEmail(dto.getEmail());
        business.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        business.setRegistrationDate(dto.getRegistrationDate());
        business.setCellPhone(dto.getCellPhone());
        business.setResetPasswordToken(dto.getResetPasswordToken());
        if (dto.getGender() != null) {
            Gender gender = new Gender();
            if (dto.getGender().getId() != null){
                gender.setId(dto.getGender().getId());
            } else {
                gender.setDescription(dto.getGender().getDescription());
            }
            business.setGender(gender);
        }

        if (dto.getAddresses() != null){
            List<Address> addressesList = addressService.listToBusiness(dto.getAddresses());
            business.setAddresses(addressesList);
        }

//        if (dto.getAddress() != null) {
//            Address address = new Address();
//            if (dto.getAddress().getId() != null){
//               address.setId(dto.getAddress().getId());
//            } else {
//                address.setCep(dto.getAddress().getCep());
//                address.setStreet(dto.getAddress().getStreet());
//                address.setNumber(dto.getAddress().getNumber());
//                address.setComplement(dto.getAddress().getComplement());
//                address.setDistrict(dto.getAddress().getDistrict());
//                address.setCity(dto.getAddress().getCity());
//                address.setUf(dto.getAddress().getUf());
//            }
//            business.setAddress(address);
//        }

        return business;
    }

    private CustomerDTO businessToDto(Customer business) {
        isDateValid(business.getBirthDate());
        CustomerDTO dto = new CustomerDTO();
        dto.setId(business.getId());
        dto.setName(business.getName());
        dto.setSurname(business.getSurname());
        dto.setCpf(business.getCpf());
        dto.setBirthDate(business.getBirthDate());
        dto.setEmail(business.getEmail());
        dto.setPassword(business.getPassword());
        dto.setRegistrationDate(business.getRegistrationDate());
        dto.setCellPhone(business.getCellPhone());
        dto.setResetPasswordToken(business.getResetPasswordToken());
        if (business.getGender() != null){
            GenderDTO genderDTO = new GenderDTO();
            genderDTO.setId(business.getGender().getId());;
            genderDTO.setDescription(business.getGender().getDescription());
            dto.setGender(genderDTO);
        }
        if (business.getAddresses() != null){
            List<AddressDTO> addressesDtoList = addressService.listToDTO(business.getAddresses());
            dto.setAddresses(addressesDtoList);
        }

//        if (business.getAddress() != null){
//            AddressDTO addressDTO = new AddressDTO();
//            addressDTO.setId(business.getAddress().getId());;
//            addressDTO.setCep(business.getAddress().getCep());
//            addressDTO.setStreet(business.getAddress().getStreet());
//            addressDTO.setNumber(business.getAddress().getNumber());
//            addressDTO.setComplement(business.getAddress().getComplement());
//            addressDTO.setDistrict(business.getAddress().getDistrict());
//            addressDTO.setCity(business.getAddress().getCity());
//            addressDTO.setUf(business.getAddress().getUf());
//            dto.setAddress(addressDTO);
//        }

        return dto;
    }


    public void deleteById(Long id) {
        if(customerRepository.existsById(id)){
            customerRepository.deleteById(id);
        }
    }

    private boolean isDateValid(LocalDate localDate) {
        LocalDate minAge = LocalDate.now().minusYears(16).plusDays(1);
        LocalDate maxAge = LocalDate.now().minusYears(130).minusDays(1);

        if (localDate.isBefore(minAge) && localDate.isAfter(maxAge)) {
            return true;
        } else if (localDate.isAfter(minAge)) {
            throw new IllegalStateException("A idade mínima para cadastro é de 16 anos.");
        } else {
            throw new IllegalStateException("A idade máxima para cadastro é de 130 anos.");
        }
    }

    // E-MAIL DE BOAS VINDAS

    @Async
    private void sendCustomerEmailWithMime(Customer customer) {
        String mailContent = "<center><a href='http://localhost:3000/home'><img src='cid:logoImage' /></a><center>";
        mailContent += "<center><h1 style='color: #392A22'>Olá, " + customer.getName() + "!</h1></center>";
        mailContent += "<center><h2 style='color: #392A22'>Seja muito bem-vindo(a) à Modo Selvagem!</h2><center>";
        mailContent += "<center><h3 style='color: #392A22'>Esperamos que goste ;)</h3><center>";
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
        email.setSubject("Bem-vindo(a) à Modo Selvagem");
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

    // BEGIN RECUPERAÇÃO DE SENHA

    public void updateResetPasswordToken(String token, String email) throws CustomerNotFoundException {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            customer.get().setResetPasswordToken(token);
            customerRepository.save(customer.get());
        } else {
            throw new CustomerNotFoundException("Não foi possível encontrar nenhum usuário com o e-mail " + email);
        }
    }

    public CustomerDTO getByResetPasswordToken(String resetPasswordToken) {
        return businessToDto(customerRepository.findByResetPasswordToken(resetPasswordToken));
    }

    public void updatePassword(CustomerDTO customerDTO, String newPassword) {
        Customer customer = dtoToBusiness(customerDTO);

        customer.setId(customerDTO.getId());
        customer.setPassword(bCryptPasswordEncoder.encode(newPassword));
        customer.setResetPasswordToken(null);

        customerRepository.save(customer);
    }

    public void sendPasswordRecoveryEmail(String email, String resetPasswordLink) {
        Optional<Customer> customer = customerRepository.findByEmail(email);

        String subject = "Redefinição de senha";

        String mailContent = "<center><a href='http://localhost:3000/home'><img src='cid:logoImage' /></a><center>";
        mailContent += "<center><h1 style='color: #392A22'>Olá, " + customer.get().getName() + "!</h1></center>";
        mailContent += "<center><h3 style='color: #392A22'>Recebemos um pedido para alteração de senha. Caso tenha solicitado clique no link abaixo:</h4><center>";
        mailContent += "<center><h3 style='color: #392A22'><a href=\"" + resetPasswordLink + "\">Criar nova senha<a/></h3><center>";
        mailContent += "<hr>";
        mailContent += "<center><h5 style='color: #838383'>© Modo Selvagem Ltda. | Todos os direitos reservados</h5><center>";
        mailContent += "<center><h6 style='color: #838383'>CNPJ: 00.000.000/0001-02 | Desde 2021</h6><center>";
        mailContent += "<center><h6 style='color: #838383'>Contato: lojamodoselvagem@gmail.com</h6><center>";

        Email newEmail = new Email();
        newEmail.setSendDateEmail(LocalDateTime.now());
        newEmail.setOwnerRef(customer.get().getId());
        newEmail.setEmailTo(email);
        newEmail.setEmailFrom("lojamodoselvagem@gmail.com");
        newEmail.setSubject(subject);
        newEmail.setText(mailContent);

        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(newEmail.getEmailFrom(), "Loja Modo Selvagem");
            helper.setTo(newEmail.getEmailTo());
            helper.setSubject(subject);
            helper.setText(mailContent, true);

            ClassPathResource resource = new ClassPathResource("/images/logoAtualizado.png");
            helper.addInline("logoImage", resource);

            emailSender.send(message);
            newEmail.setStatusEmail(StatusEmail.SENT);
        } catch (MailException | MessagingException | UnsupportedEncodingException e) {
            newEmail.setStatusEmail(StatusEmail.ERROR);
        } finally {
            emailRepository.save(newEmail);
        }
    }

    public CustomerDTO searchCustomerByEmail(String email) {
        Optional<Customer> op = customerRepository.findByEmail(email);
        if (op.isPresent()){
            return businessToDto(op.get());
        }
        throw new UsernameNotFoundException("E-mail não encontrado");
    }

}
