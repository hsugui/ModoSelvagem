package br.com.rd.ModoSelvagem.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SalePriceIdDTO {

    private Long id;
    private LocalDate validity;
}
