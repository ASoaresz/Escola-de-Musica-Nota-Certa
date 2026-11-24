package com.escola_musica.nota_certa.entity;

import jakarta.persistence.*;
import com.escola_musica.nota_certa.enums.Instrumento;
import java.math.BigDecimal;

@Entity
@Table(name = "cursos")

public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(name = "nome_curso", nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Instrumento instrumento;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoPadrao;

    @Column(nullable = false)
    private Boolean ativo;

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @PrePersist
    public void prePersist() {
        if (this.ativo == null) {
            this.ativo = true;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Instrumento getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(Instrumento instrumento) {
        this.instrumento = instrumento;
    }

    public BigDecimal getPrecoPadrao() {
        return precoPadrao;
    }

    public void setPrecoPadrao(BigDecimal precoPadrao) {
        this.precoPadrao = precoPadrao;
    }
}


