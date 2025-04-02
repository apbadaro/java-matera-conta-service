package com.bootcamp.matera.ContaService.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CONTA")
public class Conta {
    @Id // Primary Key
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @Column
    private String titular;
    @Column
    private Integer agencia;
    @Column
    private Integer numeroConta;

    @Column
    private String chavePix;

    @Column
    private BigDecimal saldo = BigDecimal.ZERO; // Inicializa o saldo com zero

    // @OneToMany => uma conta pode receber e enviar vários Pix
    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Pix> historicoPix = new ArrayList<>();

    public void sacar(BigDecimal valor) {
        saldo = this.saldo.subtract(valor);
    }

    public void depositar(BigDecimal valor) {
        saldo = this.saldo.add(valor);
    }
}
