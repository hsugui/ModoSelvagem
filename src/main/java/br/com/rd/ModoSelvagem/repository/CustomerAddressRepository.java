package br.com.rd.ModoSelvagem.repository;

import br.com.rd.ModoSelvagem.model.embeddable.CustomerAddressId;
import br.com.rd.ModoSelvagem.model.entity.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, CustomerAddressId> {
}
