package com.escola_musica.nota_certa.api.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> tratarNaoEncontrado(
            RecursoNaoEncontradoException ex
    ) {
        Map<String, Object> resposta = Map.of(
                "status", HttpStatus.NOT_FOUND.value(),
                "erro", "Not Found",
                "mensagem", ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarValidacao(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> erros = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(erro ->
                erros.putIfAbsent(
                        erro.getField(),
                        erro.getDefaultMessage()
                )
        );

        Map<String, Object> resposta = Map.of(
                "status", 400,
                "erro", "Bad Request",
                "mensagem", "Existem campos inválidos",
                "erros", erros
        );

        return ResponseEntity.badRequest().body(resposta);
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ResponseEntity<Map<String, Object>> tratarEmailDuplicado(
            EmailJaCadastradoException ex
    ) {
        Map<String, Object> resposta = Map.of(
                "status", HttpStatus.CONFLICT.value(),
                "erro", "Conflict",
                "mensagem", ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(resposta);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> tratarIntegridadeDados(
            DataIntegrityViolationException ex
    ) {
        Map<String, Object> resposta = Map.of(
                "status", HttpStatus.CONFLICT.value(),
                "erro", "Conflict",
                "mensagem", "Já existe um registro com esses dados. Verifique se o e-mail já está cadastrado."
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(resposta);
    }

    @ExceptionHandler(CodigoCursoJaCadastradoException.class)
    public ResponseEntity<Map<String, Object>> tratarCodigoCursoJaCadastrado(
            CodigoCursoJaCadastradoException ex
    ) {
        Map<String, Object> resposta = Map.of(
                "status", HttpStatus.CONFLICT.value(),
                "erro", "Conflict",
                "mensagem", ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(resposta);
    }

    @ExceptionHandler(AulaConflitoException.class)
    public ResponseEntity<Map<String, Object>> tratarAulaConflito(
            AulaConflitoException ex
    ) {
        Map<String, Object> resposta = Map.of(
                "status", HttpStatus.CONFLICT.value(),
                "erro", "Conflict",
                "mensagem", ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(resposta);
    }

    @ExceptionHandler(RecursoInativoException.class)
    public ResponseEntity<Map<String, Object>> tratarRecursoInativo(
            RecursoInativoException ex
    ) {
        Map<String, Object> resposta = Map.of(
                "status", HttpStatus.CONFLICT.value(),
                "erro", "Conflict",
                "mensagem", ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(resposta);
    }

    @ExceptionHandler(StatusAulaInvalidoException.class)
    public ResponseEntity<Map<String, Object>> tratarStatusAulaInvalido(
            StatusAulaInvalidoException ex
    ) {
        Map<String, Object> resposta = Map.of(
                "status", HttpStatus.CONFLICT.value(),
                "erro", "Conflict",
                "mensagem", ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(resposta);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> tratarCorpoInvalido(
            HttpMessageNotReadableException ex
    ) {

        String mensagem = "O corpo da requisição contém dados inválidos.";

        Throwable causa = ex.getMostSpecificCause();

        if (causa instanceof InvalidFormatException invalid
                && invalid.getTargetType().isEnum()) {

            Class<?> tipoEnum = invalid.getTargetType();

            String valoresPermitidos = Arrays.stream(tipoEnum.getEnumConstants())
                    .map(valor -> ((Enum<?>) valor).name())
                    .collect(Collectors.joining(", "));

            mensagem = "Valor inválido para o campo. Valores permitidos: "
                    + valoresPermitidos;
        }

        Map<String, Object> resposta = Map.of(
                "status", HttpStatus.BAD_REQUEST.value(),
                "erro", "Bad Request",
                "mensagem", mensagem
        );

        return ResponseEntity.badRequest().body(resposta);
    }
}
