package com.bootcamp.matera.ContaService.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ContaRequestDTO {
    @NotNull
    private String titular;
    @NotNull
    private Integer agencia;
    @NotNull
    private Integer numeroConta;
    @NotEmpty
    private String chavePix;
}

// @NotNull: impede que o campo seja nulo.
// @NotEmpty: impede que o campo seja vazio.
// @NotBlank: impede que o campo seja nulo, vazio ou apenas espaços em branco.