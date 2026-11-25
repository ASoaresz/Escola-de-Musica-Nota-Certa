package com.escola_musica.nota_certa.api.exception;

public class EmailJaCadastradoException extends RuntimeException {

    public EmailJaCadastradoException(String mensagem) {
        super(mensagem);
    }
}