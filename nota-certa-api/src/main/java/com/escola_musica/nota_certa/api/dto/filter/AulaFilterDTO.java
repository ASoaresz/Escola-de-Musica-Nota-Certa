package com.escola_musica.nota_certa.api.dto.filter;

import com.escola_musica.nota_certa.enums.Modalidade;
import com.escola_musica.nota_certa.enums.StatusAula;

import java.time.LocalDateTime;

public record AulaFilterDTO(
        StatusAula status,
        Modalidade modalidade,
        Long alunoId,
        Long cursoId,
        LocalDateTime dataInicio,
        LocalDateTime dataFim
) {}