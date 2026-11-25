package com.escola_musica.nota_certa.api.dto.response;

import java.time.LocalDateTime;

public record AlunoResponseDTO(
        Long id,
        String nome,
        String email,
        String telefone,
        String cidade,
        LocalDateTime dataCadastro,
        Boolean ativo
) {
}

