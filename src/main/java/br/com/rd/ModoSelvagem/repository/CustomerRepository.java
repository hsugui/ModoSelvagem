package br.com.rd.ModoSelvagem.repository;

import br.com.rd.ModoSelvagem.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByCpf(String cpf);
    Customer findByResetPasswordToken(String token);
}
