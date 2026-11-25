package com.escola_musica.nota_certa.api.dto.response;

import com.escola_musica.nota_certa.enums.Modalidade;
import com.escola_musica.nota_certa.enums.StatusAula;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AulaResponseDTO(
        Long id,
        Long alunoId,
        String nomeAluno,
        Long cursoId,
        String nomeCurso,
        LocalDateTime dataHora,
        Modalidade modalidade,
        BigDecimal valor,
        StatusAula status
) {}