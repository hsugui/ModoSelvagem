package br.com.rd.ModoSelvagem.controller;

import br.com.rd.ModoSelvagem.exceptions.CustomerNotFoundException;
import br.com.rd.ModoSelvagem.model.dto.CustomerDTO;
import br.com.rd.ModoSelvagem.model.dto.NewPasswordDTO;
import br.com.rd.ModoSelvagem.model.dto.PasswordRecoveryDTO;
import br.com.rd.ModoSelvagem.service.CustomerService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public PasswordRecoveryDTO sendPasswordRecoveryEmail(@RequestBody PasswordRecoveryDTO emailForRecovery) {
        String email = emailForRecovery.getEmail();
        String token = RandomString.make(45);

        customerService.searchCustomerByEmail(email);

        try {
            customerService.updateResetPasswordToken(token, email);

            String resetPasswordLink = "http://localhost:3000/reset/" + token;

            customerService.sendPasswordRecoveryEmail(email, resetPasswordLink);

        } catch (CustomerNotFoundException e) {
            e.getMessage();
        }
        return emailForRecovery;
    }

    @GetMapping("/reset")
    public CustomerDTO searchByToken(@Param(value = "token") String token) {
        return customerService.getByResetPasswordToken(token);
    }

    @PostMapping("/reset/{token}")
    public NewPasswordDTO resetPassword(@RequestBody NewPasswordDTO passwordDTO, @PathVariable("token") String token) {
        String password = passwordDTO.getPassword();
        CustomerDTO customerDTO = customerService.getByResetPasswordToken(token);

        if (customerDTO == null) {
            throw new IllegalArgumentException("Token inválido");
        } else {
            customerService.updatePassword(customerDTO, password);
        }
        return passwordDTO;
    }

}
