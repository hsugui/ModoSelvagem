package br.com.rd.ModoSelvagem.service;

import br.com.rd.ModoSelvagem.enums.StatusEmail;
import br.com.rd.ModoSelvagem.model.dto.ContactDTO;
import br.com.rd.ModoSelvagem.model.entity.Contact;
import br.com.rd.ModoSelvagem.model.entity.Customer;
import br.com.rd.ModoSelvagem.model.entity.Email;
import br.com.rd.ModoSelvagem.repository.ContactRepository;
import br.com.rd.ModoSelvagem.repository.EmailRepository;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContactService {
    @Autowired
    ContactRepository contactRepository;

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private ContactMapper contactMapper;

    public ContactDTO create(ContactDTO contact) {
        Contact newContact = contactMapper.dtoToBusiness(contact);
        newContact.setDate(LocalDateTime.now());
        newContact = contactRepository.save(newContact);

//        sendContactEmail(newContact);
        sendContactEmailWithMime(newContact);

        return contactMapper.businessToDTO(newContact);
    }

    public ContactDTO findById(Long id) {
        Optional<Contact> contact = contactRepository.findById(id);

        if (contact.isPresent()) {
            return contactMapper.businessToDTO(contact.get());
        }
        return null;
    }

    public List<ContactDTO> findAll() {
        List<Contact> contactList = contactRepository.findAll();
        return contactMapper.listToDto(contactList);
    }

    public List<ContactDTO> findAllByEmail(String email) {
        List<Contact> contactList = contactRepository.findAllByEmail(email);
        return contactMapper.listToDto(contactList);
    }

    public List<ContactDTO> findAllByDate(Date date) {
        List<Contact> contactList = contactRepository.findAllByDate(date);
        return contactMapper.listToDto(contactList);
    }

    public ContactDTO updateById(ContactDTO dto, Long id) {
        Optional<Contact> op = contactRepository.findById(id);

        if (op.isPresent()) {
            Contact obj = op.get();

            if (dto.getEmail() != null) {
                obj.setEmail(dto.getEmail());
            }

            if (dto.getName() != null) {
                obj.setName(dto.getName());
            }

            if (dto.getMessage() != null) {
                obj.setMessage(dto.getMessage());
            }

            contactRepository.save(obj);
            return contactMapper.businessToDTO(obj);
        }
        return null;
    }

//    @Async
//    private void sendContactEmail(Contact contact) {
//        Email email = new Email();
//        email.setSendDateEmail(LocalDateTime.now());
//        email.setOwnerRef(contact.getId());
//        email.setEmailTo(contact.getEmail());
//        email.setEmailFrom("lojamodoselvagem@gmail.com");
//        email.setSubject("Contato @ Modo Selvagem");
//        email.setText(String.format("Olá, %s! Recebemos a sua mensagem e em breve retornaremos o contato.", contact.getName()));
//        try{
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setFrom(email.getEmailFrom());
//            message.setTo(email.getEmailTo());
//            message.setSubject(email.getSubject());
//            message.setText(email.getText());
//            emailSender.send(message);
//            email.setStatusEmail(StatusEmail.SENT);
//        } catch (MailException e){
//            email.setStatusEmail(StatusEmail.ERROR);
//        } finally {
//            emailRepository.save(email);
//        }
//    }

    @Async
    private void sendContactEmailWithMime(Contact contact) {
        String mailContent = "<center><a href='http://localhost:3000/home'><img src='cid:logoImage' /></a><center>";
        mailContent += "<center><h1 style='color: #392A22'>Olá, " + contact.getName() + "!</h1></center>";
        mailContent += "<center><h2 style='color: #392A22'>Recebemos sua mensagem:</h2><center>";
        mailContent += "<center><p style='color: #392A22'>" + contact.getMessage() + "</p><center>";
        mailContent += "<center><h3 style='color: #392A22'>Assim que possível, entraremos em contato pelo e-mail:</h3>" + contact.getEmail() + "<center>";
        mailContent += "<center><h4 style='color: #392A22'>Agradecemos a preferência!</h4><center>";
        mailContent += "<hr>";
        mailContent += "<center><h5 style='color: #838383'>© Modo Selvagem Ltda. | Todos os direitos reservados</h5><center>";
        mailContent += "<center><h6 style='color: #838383'>CNPJ: 00.000.000/0001-02 | Desde 2021</h6><center>";
        mailContent += "<center><h6 style='color: #838383'>Contato: lojamodoselvagem@gmail.com</h6><center>";

        Email email = new Email();
        email.setSendDateEmail(LocalDateTime.now());
        email.setOwnerRef(contact.getId());
        email.setEmailTo(contact.getEmail());
        email.setEmailFrom("lojamodoselvagem@gmail.com");
        email.setSubject("Contato @ Modo Selvagem");
        email.setText(mailContent);

        String[] emailToArray = {email.getEmailTo(), "lojamodoselvagem@gmail.com"};

        try{
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(email.getEmailFrom(), "Loja Modo Selvagem");
            helper.setTo(emailToArray);
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
}
