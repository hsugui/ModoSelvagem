package br.com.rd.ModoSelvagem.repository;

import br.com.rd.ModoSelvagem.model.embeddable.ItemOrderId;
import br.com.rd.ModoSelvagem.model.entity.ItemOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemOrderRepository extends JpaRepository<ItemOrder, ItemOrderId> {
}
