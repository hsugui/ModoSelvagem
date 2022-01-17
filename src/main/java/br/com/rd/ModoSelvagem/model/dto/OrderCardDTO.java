package br.com.rd.ModoSelvagem.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderCardDTO {
    private Long id;
    @JsonFormat(pattern = "dd/MM/yyyy – HH:mm:ss")
    private LocalDateTime orderDate;
    private Double totalPrice;
    private OrderStatusDTO status;
}
