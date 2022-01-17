package br.com.rd.ModoSelvagem.repository;

import br.com.rd.ModoSelvagem.model.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
}
