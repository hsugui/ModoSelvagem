package br.com.rd.ModoSelvagem.model.embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAddressId implements Serializable {
    private Long customerId;
    private Long addressId;
}
