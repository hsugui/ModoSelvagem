package br.com.rd.ModoSelvagem.repository;

import br.com.rd.ModoSelvagem.model.entity.SalePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalePriceRepository extends JpaRepository<SalePrice, Long> {
}
