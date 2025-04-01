package com.bootcamp.matera.ContaService.service;

import com.bootcamp.matera.ContaService.dto.ContaDTO;
import com.bootcamp.matera.ContaService.dto.ContaRequestDTO;
import com.bootcamp.matera.ContaService.dto.ContaResponseDTO;
import com.bootcamp.matera.ContaService.exception.ContaExistenteException;
import com.bootcamp.matera.ContaService.exception.ContaNaoExistenteException;
import com.bootcamp.matera.ContaService.model.Conta;
import com.bootcamp.matera.ContaService.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContaService {
    private final ContaRepository contaRepository;

    public ContaResponseDTO criarConta(ContaRequestDTO contaRequestDTO) {
        Optional<Conta> contaOptional = contaRepository.findByTitularAndNumeroContaAndChavePix(
                contaRequestDTO.getTitular(),
                contaRequestDTO.getNumeroConta(),
                contaRequestDTO.getChavePix()
        );

        if (contaOptional.isPresent()) {
            throw new ContaExistenteException("Conta já existe no sistema.");
        }

        Conta conta = Conta.builder()
                // Aqui, usamos o builder para criar o objeto Conta
                // com os dados recebidos no request
                // e usamos o metodo builder() para criar uma nova instância de Conta.
                .titular(contaRequestDTO.getTitular())
                .agencia(contaRequestDTO.getAgencia())
                .numeroConta(contaRequestDTO.getNumeroConta())
                .chavePix(contaRequestDTO.getChavePix())
                .saldo(new BigDecimal(5000))
                .build();

        Conta contaSalva = contaRepository.save(conta);

        ContaResponseDTO contaResponseDTO = ContaResponseDTO.builder()
                // Aqui, usamos o builder para criar o objeto ContaResponseDTO
                // com os dados da conta que acabamos de salvar.

                .id(contaSalva.getId())
                .titular(contaRequestDTO.getTitular())
                .build();

        log.info("ContaResponseDTO: {}", contaResponseDTO);

        return contaResponseDTO;
    }
    public List<ContaDTO> buscaTodasContas() {
        // Aqui, usamos o meodo findAll() do JpaRepository para buscar todas as contas;
        // depois usamos o stream para mapear cada Conta para ContaDTO
        // e reunir os resultados em uma lista.

        List<ContaDTO> contas = contaRepository.findAll().stream().map(
                        conta -> ContaDTO.builder()
                                .id(conta.getId())
                                .titular(conta.getTitular())
                                .agencia(conta.getAgencia())
                                .numeroConta(conta.getNumeroConta())
                                .chavePix(conta.getChavePix())
                                .saldo(conta.getSaldo())
                                .build())
                .toList();

        return contas;

    }

    public ContaDTO buscaContaById(UUID id) {
        /**
         * Aqui, usamos o metodo findById() do JpaRepository para buscar a conta pelo id;
         * depois usamos o map para transformar a Conta em ContaDTO
         * e o orElseThrow para lançar uma exceção caso a conta não exista.
         * O metodo orElseThrow() é usado para lançar uma exceção caso a conta não seja encontrada.
         * O metodo map() é usado para transformar a Conta em ContaDTO.
         * O metodo orElseThrow() é usado para lançar uma exceção caso a conta não seja encontrada.
         */

        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new ContaNaoExistenteException("Conta não existe."));

        ContaDTO contaDTO = ContaDTO.builder()
                .id(conta.getId())
                .titular(conta.getTitular())
                .agencia(conta.getAgencia())
                .numeroConta(conta.getNumeroConta())
                .chavePix(conta.getChavePix())
                .saldo(conta.getSaldo())
                .build();

        return contaDTO;
    }

    public ContaResponseDTO atualizarConta(ContaRequestDTO contaRequestDTO, UUID id) {

        Conta contaExistente = contaRepository.findById(id)
                .orElseThrow(() -> new ContaNaoExistenteException("Conta não existe."));

        contaExistente.setTitular(contaRequestDTO.getTitular());
        contaExistente.setNumeroConta(contaRequestDTO.getNumeroConta());
        contaExistente.setAgencia(contaRequestDTO.getAgencia());
        contaExistente.setChavePix(contaRequestDTO.getChavePix());

        contaExistente = contaRepository.save(contaExistente);

        return ContaResponseDTO.builder()
                .id(contaExistente.getId())
                .titular(contaExistente.getTitular())
                .build();
    }

    public void apagarConta(UUID id) {
        // Aqui, usamos o metodo findById() do JpaRepository para buscar a conta pelo id;
        // O metodo deleteById() é usado para apagar a conta pelo id.

        contaRepository.findById(id).orElseThrow(() -> new ContaNaoExistenteException("Conta não existe."));

        contaRepository.deleteById(id);
    }
}
