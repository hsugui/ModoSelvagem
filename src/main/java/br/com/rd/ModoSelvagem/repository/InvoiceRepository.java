package br.com.rd.ModoSelvagem.repository;

import br.com.rd.ModoSelvagem.model.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query(value = "SELECT * FROM tb_invoice ORDER BY issue_date DESC LIMIT 1", nativeQuery = true)
    Invoice findLastInvoice();

    Optional<Invoice> findByOrderId(Long id);

}
