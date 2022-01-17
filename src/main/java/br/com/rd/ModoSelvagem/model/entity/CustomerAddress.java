package br.com.rd.ModoSelvagem.model.entity;

import br.com.rd.ModoSelvagem.model.embeddable.CustomerAddressId;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Entity(name = "TB_CUSTOMER_ADDRESS")
@Data
@IdClass(CustomerAddressId.class)
public class CustomerAddress implements Serializable {

    @Id
    @Column(name = "customer_id")
    private Long customerId;

    @Id
    @Column(name = "address_id")
    private Long addressId;

//    @Column(nullable = false)
//    private Boolean main;

}

