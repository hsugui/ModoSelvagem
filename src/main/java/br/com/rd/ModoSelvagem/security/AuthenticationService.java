package br.com.rd.ModoSelvagem.security;

import br.com.rd.ModoSelvagem.model.entity.Customer;
import br.com.rd.ModoSelvagem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private CustomerRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> customer = repository.findByEmail(username);
        if (customer.isPresent()) {
            return customer.get();
        }

        throw new UsernameNotFoundException(String.format("O usuário com o e-mail %s não foi encontrado.", username));
    }

}
