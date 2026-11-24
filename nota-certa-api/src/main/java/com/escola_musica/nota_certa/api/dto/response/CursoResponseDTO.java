package com.escola_musica.nota_certa.api.dto.response;

import com.escola_musica.nota_certa.enums.Instrumento;

import java.math.BigDecimal;

public record CursoResponseDTO(
        Long id,
        String codigo,
        String nome,
        Instrumento instrumento,
        BigDecimal precoPadrao,
        Boolean ativo
) {
}