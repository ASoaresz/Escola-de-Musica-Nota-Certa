package com.escola_musica.nota_certa.api.exception;

public class AulaConflitoException extends RuntimeException {

    public AulaConflitoException(String mensagem) {
        super(mensagem);
    }
}