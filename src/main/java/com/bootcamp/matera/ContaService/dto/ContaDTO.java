package com.bootcamp.matera.ContaService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class ContaDTO {

    // no DTO, colocamos apenas os dados que queremos expor para o cliente

    private UUID id;
    private String titular;
    private Integer agencia;
    private Integer numeroConta;
    private String chavePix;
    private BigDecimal saldo;
}
