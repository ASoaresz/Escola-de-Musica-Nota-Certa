package com.escola_musica.nota_certa.repository.specification;

import com.escola_musica.nota_certa.entity.Aluno;
import org.springframework.data.jpa.domain.Specification;

public class AlunoSpecification {

    public static Specification<Aluno> nomeContem(String nome) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nome")),
                        "%" + nome.toLowerCase() + "%"
                );
    }

    public static Specification<Aluno> cidadeIgual(String cidade) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("cidade")),
                        cidade.toLowerCase()
                );
    }
}