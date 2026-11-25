package com.escola_musica.nota_certa.repository;

import com.escola_musica.nota_certa.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AlunoRepository extends
        JpaRepository<Aluno, Long>,
        JpaSpecificationExecutor<Aluno> {
    boolean existsByEmail(String email);
}

