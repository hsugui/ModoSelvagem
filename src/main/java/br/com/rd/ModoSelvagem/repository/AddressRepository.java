package br.com.rd.ModoSelvagem.repository;

import br.com.rd.ModoSelvagem.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
