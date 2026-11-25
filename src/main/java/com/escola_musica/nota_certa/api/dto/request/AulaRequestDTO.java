package com.escola_musica.nota_certa.api.dto.request;

import com.escola_musica.nota_certa.enums.Modalidade;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AulaRequestDTO(

        @NotNull(message = "O ID do aluno é obrigatório")
        Long alunoId,

        @NotNull(message = "O ID do curso é obrigatório")
        Long cursoId,

        @NotNull(message = "A data e hora são obrigatórias")
        LocalDateTime dataHora,

        @NotNull(message = "A modalidade é obrigatória")
        Modalidade modalidade,

        @NotNull(message = "O valor da aula é obrigatório")
        @DecimalMin(value = "0.00", inclusive = false,
                message = "O valor deve ser maior que zero")
        @Digits(integer = 8, fraction = 2,
                message = "O valor deve ter até 2 casas decimais")
        BigDecimal valor

) {}