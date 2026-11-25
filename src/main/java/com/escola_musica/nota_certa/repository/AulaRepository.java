package com.escola_musica.nota_certa.repository;

import com.escola_musica.nota_certa.entity.Aula;
import com.escola_musica.nota_certa.enums.StatusAula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;

public interface AulaRepository extends
        JpaRepository<Aula, Long>,
        JpaSpecificationExecutor<Aula> {

    boolean existsByAlunoIdAndDataHoraAndStatus(
            Long alunoId,
            LocalDateTime dataHora,
            StatusAula status
    );

}