package com.bootcamp.matera.ContaService.service;

import com.bootcamp.matera.ContaService.dto.PixDTO;
import com.bootcamp.matera.ContaService.dto.PixRequestDTO;
import com.bootcamp.matera.ContaService.dto.PixResponseDTO;
import com.bootcamp.matera.ContaService.exception.ContaNaoExistenteException;
import com.bootcamp.matera.ContaService.exception.SaldoInsuficienteException;
import com.bootcamp.matera.ContaService.model.Conta;
import com.bootcamp.matera.ContaService.model.Pix;
import com.bootcamp.matera.ContaService.repository.ContaRepository;
import com.bootcamp.matera.ContaService.repository.PixRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PixService {
        private final PixRepository pixRepository;
        private final ContaRepository contaRepository;

        @Transactional
        public PixResponseDTO realizarPix(PixRequestDTO pixRequestDTO) {

            Optional<Pix> existingPix = pixRepository.findByIdempotencia(pixRequestDTO.getIdempotencia());
            // verifica se já existe um PIX com a idempotência informada
            // se existir, retorna a informação do PIX já realizado
            if (existingPix.isPresent()) {
                return new PixResponseDTO(
                        existingPix.get().getCreatedAt(),
                        "PIX já realizado com a idempotência informada",
                        existingPix.map(this::entityToDto).get()
                );
            }

            Optional<Conta> contaPagadorOptional = contaRepository.findByChavePix(pixRequestDTO.getChavePixPagador());

            if(contaPagadorOptional.isEmpty()) {
                throw new ContaNaoExistenteException(String.format("Conta com a chave '%s' não existe", pixRequestDTO.getChavePixPagador()));
            }

            Optional<Conta> contaReceberOptional = contaRepository.findByChavePix(pixRequestDTO.getChavePixRecebedor());

            if(contaReceberOptional.isEmpty()) {
                throw new ContaNaoExistenteException(String.format("Conta com a chave '%s' não existe", pixRequestDTO.getChavePixRecebedor()));
            }

            Conta contaPagador = contaPagadorOptional.get();
            Conta contaRecebedor = contaReceberOptional.get();

            if(pixRequestDTO.getValor().compareTo(contaPagador.getSaldo()) > 0) {
                throw new SaldoInsuficienteException("Saldo insuficiente para realizar o PIX");
            }

            contaPagador.sacar(pixRequestDTO.getValor());
            contaRecebedor.depositar(pixRequestDTO.getValor());

            contaRepository.save(contaRecebedor);
            contaRepository.save(contaPagador);

            Pix pix = Pix.builder()
                    .chavePixPagador(pixRequestDTO.getChavePixPagador())
                    .chavePixRecebedor(pixRequestDTO.getChavePixRecebedor())
                    .conta(contaPagador)
                    .valor(pixRequestDTO.getValor())
                    .idempotencia(pixRequestDTO.getIdempotencia())
                    .createdAt(LocalDateTime.now())
                    .build();

            pixRepository.save(pix);

            return PixResponseDTO.builder()
                    .pix(entityToDto(pix))
                    .createdAt(pix.getCreatedAt())
                    .message("PIX realizado com sucesso!")
                    .build();
    }

    private PixDTO entityToDto(Pix pix) {

            // converte a entidade Pix para o DTO PixDTO
            // e retorna o DTO

        return PixDTO.builder()
                .id(pix.getId())
                .chavePixPagador(pix.getChavePixPagador())
                .chavePixRecebedor(pix.getChavePixRecebedor())
                .valor(pix.getValor())
                .idempotencia(pix.getIdempotencia())
                .build();
    }
}
