package br.com.rd.ModoSelvagem.repository;

import br.com.rd.ModoSelvagem.model.embeddable.InvoiceItemId;
import br.com.rd.ModoSelvagem.model.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, InvoiceItemId> {
}
