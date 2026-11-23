package com.escola_musica.nota_certa.repository;

import com.escola_musica.nota_certa.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CursoRepository extends
        JpaRepository<Curso, Long>,
        JpaSpecificationExecutor<Curso> {

    boolean existsByCodigo(String codigo);
}