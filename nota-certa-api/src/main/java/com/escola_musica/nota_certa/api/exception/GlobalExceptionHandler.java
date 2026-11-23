package com.escola_musica.nota_certa.api.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

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
}
