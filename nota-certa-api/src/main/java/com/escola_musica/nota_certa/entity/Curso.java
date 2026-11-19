package com.escola_musica.nota_certa.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cursos")

public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Instrumento instrumento;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoPadrao;

    private Boolean ativo;

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @PrePersist
    public void prePersist() {
        this.ativo = true;
    }

}


