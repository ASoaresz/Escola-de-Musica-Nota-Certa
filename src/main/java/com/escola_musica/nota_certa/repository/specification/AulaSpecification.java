package com.escola_musica.nota_certa.repository.specification;

import com.escola_musica.nota_certa.entity.Aula;
import com.escola_musica.nota_certa.enums.Modalidade;
import com.escola_musica.nota_certa.enums.StatusAula;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class AulaSpecification {
    public static Specification<Aula> statusIgual(StatusAula status) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("status"),
                        status
                );
    }

    public static Specification<Aula> modalidadeIgual(Modalidade modalidade) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("modalidade"),
                        modalidade
                );
    }

    public static Specification<Aula> alunoIgual(Long alunoId) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("aluno").get("id"),
                        alunoId
                );
    }

    public static Specification<Aula> cursoIgual(Long cursoId) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("curso").get("id"),
                        cursoId
                );
    }

    public static Specification<Aula> dataHoraMaiorOuIgual(LocalDateTime dataInicio) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("dataHora"),
                        dataInicio
                );
    }

    public static Specification<Aula> dataHoraMenorOuIgual(LocalDateTime dataFim) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("dataHora"),
                        dataFim
                );
    }
}