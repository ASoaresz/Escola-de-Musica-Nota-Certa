package com.escola_musica.nota_certa.api.dto.request;

import com.escola_musica.nota_certa.enums.StatusAula;
import jakarta.validation.constraints.NotNull;

public record StatusAulaRequestDTO(

        @NotNull(message = "O novo status é obrigatório")
        StatusAula status

) {}