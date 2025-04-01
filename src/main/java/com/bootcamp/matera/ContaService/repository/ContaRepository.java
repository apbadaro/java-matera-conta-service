package com.bootcamp.matera.ContaService.repository;

import com.bootcamp.matera.ContaService.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ContaRepository extends JpaRepository<Conta, UUID> {
    Optional<Conta> findByChavePix(String chavePixPagador);

    Optional<Conta> findByTitularAndNumeroContaAndChavePix(String titular, Integer numeroConta, String chavePixPagador);
}
