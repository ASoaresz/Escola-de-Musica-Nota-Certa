package com.escola_musica.nota_certa.api.dto.request;

import com.escola_musica.nota_certa.entity.Instrumento;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CursoRequestDTO(

        @NotBlank(message = "O código é obrigatório")
        @Size(min = 2, max = 20, message = "O código deve ter entre 2 e 20 caracteres")
        String codigo,

        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 caracteres")
        String nome,

        @NotNull(message = "O instrumento é obrigatório")
        Instrumento instrumento,

        @NotNull(message = "O preço padrão é obrigatório")
        @DecimalMin(value = "0.00", inclusive = false, message = "O preço deve ser maior que zero")
        @Digits(integer = 8, fraction = 2, message = "O preço deve ter até 2 casas decimais")
        BigDecimal precoPadrao,

        Boolean ativo

) {
}