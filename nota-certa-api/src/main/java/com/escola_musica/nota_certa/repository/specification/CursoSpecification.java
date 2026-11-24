
package com.escola_musica.nota_certa.repository.specification;

import com.escola_musica.nota_certa.entity.Curso;
import com.escola_musica.nota_certa.enums.Instrumento;
import org.springframework.data.jpa.domain.Specification;

public class CursoSpecification {

    public static Specification<Curso> nomeContem(String nome) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nome")),
                        "%" + nome.toLowerCase() + "%"
                );
    }

    public static Specification<Curso> instrumentoIgual(
            Instrumento instrumento
    ) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("instrumento"),
                        instrumento
                );
    }
}
