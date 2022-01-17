package br.com.rd.ModoSelvagem.repository;

import br.com.rd.ModoSelvagem.model.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {
}
