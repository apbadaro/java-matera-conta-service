package com.bootcamp.matera.ContaService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PixResponseDTO {
    private LocalDateTime createdAt; // data de criação do Pix
    private String message; // mensagem de sucesso ou erro
    private PixDTO pix; // DTO com todos os dados do Pix
}
