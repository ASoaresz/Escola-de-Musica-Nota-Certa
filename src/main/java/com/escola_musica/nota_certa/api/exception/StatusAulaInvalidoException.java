package com.escola_musica.nota_certa.api.exception;

public class StatusAulaInvalidoException extends RuntimeException {

    public StatusAulaInvalidoException(String mensagem) {
        super(mensagem);
    }
}