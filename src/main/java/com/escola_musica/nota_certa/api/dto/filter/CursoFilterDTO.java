package com.escola_musica.nota_certa.api.dto.filter;

import com.escola_musica.nota_certa.enums.Instrumento;

public record CursoFilterDTO(
        String nome,
        Instrumento instrumento
) {
}