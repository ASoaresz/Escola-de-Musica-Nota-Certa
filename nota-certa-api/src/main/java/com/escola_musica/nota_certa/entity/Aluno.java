package com.escola_musica.nota_certa.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "alunos")

public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 250)
    private String email;

    @Column(nullable = false, length = 11)
    private String telefone;

    @Column(nullable = false, length = 80)
    private String cidade;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

    private Boolean ativo;

}
