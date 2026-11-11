package org.example;

public class ErroAoSalvarProdutoException extends RuntimeException {
    public ErroAoSalvarProdutoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}